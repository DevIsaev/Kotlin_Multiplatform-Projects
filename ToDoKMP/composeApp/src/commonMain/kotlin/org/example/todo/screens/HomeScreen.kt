package org.example.todo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.example.todo.components.ErrorScreen
import org.example.todo.components.LoadingScreen
import org.example.todo.components.TaskView
import org.example.todo.data.InMemoryStorage
import org.example.todo.domain.RequestState
import org.example.todo.domain.ToDoTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    storage: InMemoryStorage,
    onTaskClick: (ToDoTask) -> Unit,
    onAddTask: () -> Unit
) {
    var activeTasksState by remember { mutableStateOf<RequestState<List<ToDoTask>>>(RequestState.Loading) }
    var completedTasksState by remember { mutableStateOf<RequestState<List<ToDoTask>>>(RequestState.Loading) }
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<ToDoTask?>(null) }

    // Загрузка задач
    LaunchedEffect(Unit) {
        loadTasks(storage, activeTasksState, completedTasksState) { active, completed ->
            activeTasksState = active
            completedTasksState = completed
        }
    }

    // Диалог удаления
    if (showDialog && taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Удалить задачу") },
            text = { Text("Вы уверены, что хотите удалить задачу \"${taskToDelete?.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToDelete?.let { task ->
                            runBlocking {
                                storage.deleteTask(task)
                                loadTasks(storage, activeTasksState, completedTasksState) { active, completed ->
                                    activeTasksState = active
                                    completedTasksState = completed
                                }
                            }
                        }
                        showDialog = false
                        taskToDelete = null
                    }
                ) { Text("Да") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        taskToDelete = null
                    }
                ) { Text("Нет") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мои задачи") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Добавить задачу",)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Активные задачи
            Text(
                text = "Активные задачи",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            when (val state = activeTasksState) {
                is RequestState.Loading -> LoadingScreen()
                is RequestState.Error -> ErrorScreen(state.message)
                is RequestState.Success -> {
                    if (state.data.isNotEmpty()) {
                        LazyColumn {
                            items(state.data) { task ->
                                TaskView(
                                    task = task,
                                    showActive = true,
                                    onSelect = { onTaskClick(task) },
                                    onComplete = { selectedTask, completed ->
                                        runBlocking {
                                            storage.setCompleted(selectedTask, completed)
                                            loadTasks(storage, activeTasksState, completedTasksState) { active, completed ->
                                                activeTasksState = active
                                                completedTasksState = completed
                                            }
                                        }
                                    },
                                    onFavorite = { selectedTask, favourite ->
                                        runBlocking {
                                            storage.setFavourite(selectedTask, favourite)
                                            loadTasks(storage, activeTasksState, completedTasksState) { active, completed ->
                                                activeTasksState = active
                                                completedTasksState = completed
                                            }
                                        }
                                    },
                                    onDelete = { selectedTask ->
                                        taskToDelete = selectedTask
                                        showDialog = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        ErrorScreen("Нет активных задач")
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Завершенные задачи
            Text(
                text = "Завершенные задачи",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            when (val state = completedTasksState) {
                is RequestState.Loading -> LoadingScreen()
                is RequestState.Error -> ErrorScreen(state.message)
                is RequestState.Success -> {
                    if (state.data.isNotEmpty()) {
                        LazyColumn {
                            items(state.data) { task ->
                                TaskView(
                                    task = task,
                                    showActive = false,
                                    onSelect = { },
                                    onComplete = { selectedTask, completed ->
                                        runBlocking {
                                            storage.setCompleted(selectedTask, completed)
                                            loadTasks(storage, activeTasksState, completedTasksState) { active, completed ->
                                                activeTasksState = active
                                                completedTasksState = completed
                                            }
                                        }
                                    },
                                    onFavorite = { _, _ -> },
                                    onDelete = { selectedTask ->
                                        taskToDelete = selectedTask
                                        showDialog = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        ErrorScreen("Нет завершенных задач")
                    }
                }
                else -> {}
            }
        }
    }
}

private suspend fun loadTasks(
    storage: InMemoryStorage,
    currentActive: RequestState<List<ToDoTask>>,
    currentCompleted: RequestState<List<ToDoTask>>,
    onResult: (RequestState<List<ToDoTask>>, RequestState<List<ToDoTask>>) -> Unit
) {
    val activeTasks = storage.readAllTasks()
    val completedTasks = storage.readCompletedTasks()

    activeTasks.collect { activeState ->
        completedTasks.collect { completedState ->
            onResult(activeState, completedState)
        }
    }
}