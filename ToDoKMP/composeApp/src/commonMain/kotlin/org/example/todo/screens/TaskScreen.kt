package org.example.todo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.example.todo.data.InMemoryStorage
import org.example.todo.domain.ToDoTask

const val DEFAULT_TITLE = ""
const val DEFAULT_DESC = ""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    storage: InMemoryStorage,
    task: ToDoTask?,
    onBack: () -> Unit
) {
    // Локальные состояния для полей ввода
    var currentTitle by remember {
        mutableStateOf(task?.title.takeIf { !it.isNullOrBlank() } ?: DEFAULT_TITLE)
    }
    var currentDesc by remember {
        mutableStateOf(task?.description.takeIf { !it.isNullOrBlank() } ?: DEFAULT_DESC)
    }

    // Проверка на заполненность полей
    val isFormValid = currentTitle.isNotBlank() && currentTitle != DEFAULT_TITLE &&
            currentDesc.isNotBlank() && currentDesc != DEFAULT_DESC

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = currentTitle,
                        onValueChange = { currentTitle = it },
                        textStyle = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                innerTextField()
                                if (currentTitle.isEmpty()) {
                                    Text(
                                        text = "Введите заголовок...",
                                        style = TextStyle(
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (isFormValid) {
                FloatingActionButton(
                    onClick = {
                        runBlocking {
                            if (task != null) {
                                // Обновляем существующую задачу
                                val updatedTask = task.copy(
                                    title = currentTitle,
                                    description = currentDesc
                                )
                                storage.updateTask(updatedTask)
                            } else {
                                // Создаем новую задачу
                                val newTask = ToDoTask(
                                    id = 0,
                                    title = currentTitle,
                                    description = currentDesc,
                                    favourite = false,
                                    completed = false
                                )
                                storage.addTask(newTask)
                            }
                        }
                        onBack()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Сохранить")
                }
            }
        }
    ) { paddingValues ->
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            value = currentDesc,
            onValueChange = { currentDesc = it },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    innerTextField()
                    if (currentDesc.isEmpty()) {
                        Text(
                            text = "Введите описание...",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        )
    }
}