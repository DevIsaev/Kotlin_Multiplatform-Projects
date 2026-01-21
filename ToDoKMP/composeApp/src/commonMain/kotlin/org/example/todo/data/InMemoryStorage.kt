package org.example.todo.data


import androidx.compose.runtime.mutableStateListOf
import com.russhwolf.settings.Settings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.todo.domain.RequestState
import org.example.todo.domain.TaskStorage
import org.example.todo.domain.ToDoTask
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch

class InMemoryStorage: TaskStorage {
    private val _tasks = mutableListOf<ToDoTask>()
    private val _tasksFlow = MutableStateFlow<List<ToDoTask>>(emptyList())
    @OptIn(ExperimentalAtomicApi::class)
    private val currentId = AtomicInt(0) // Атомарный счетчик для многопоточности

    init {
        // Добавляем несколько тестовых задач для примера
        val initialTasks = listOf(
            ToDoTask(
                id = getNextId(),
                title = "Купить продукты",
                description = "Молоко, хлеб, яйца",
                completed = false,
                favourite = true
            ),
            ToDoTask(
                id = getNextId(),
                title = "Сделать домашку",
                description = "Математика и физика",
                completed = true,
                favourite = false
            ),
            ToDoTask(
                id = getNextId(),
                title = "Позвонить маме",
                description = "День рождения",
                completed = false,
                favourite = false
            )
        )

        _tasks.addAll(initialTasks)
        _tasksFlow.value = _tasks.toList()
    }

    // Простая функция для получения следующего ID
    @OptIn(ExperimentalAtomicApi::class)
    private fun getNextId(): Int {
        return currentId.incrementAndFetch()
    }

    override fun readAllTasks(): Flow<RequestState<List<ToDoTask>>> = flow {
        emit(RequestState.Loading)
        delay(100) // Имитация загрузки

        // Создаем копию списка для безопасности
        val tasksCopy = _tasks.toList()
        val activeTasks = tasksCopy
            .filter { !it.completed }
            .sortedByDescending { it.favourite }

        emit(RequestState.Success(activeTasks))
    }

    override fun readCompletedTasks(): Flow<RequestState<List<ToDoTask>>> = flow {
        emit(RequestState.Loading)
        delay(100) // Имитация загрузки

        // Создаем копию списка для безопасности
        val tasksCopy = _tasks.toList()
        val completedTasks = tasksCopy.filter { it.completed }

        emit(RequestState.Success(completedTasks))
    }

    override suspend fun addTask(task: ToDoTask) {
        // Генерируем новый ID если задача новая (id = 0)
        val newTask = if (task.id == 0) {
            task.copy(id = getNextId())
        } else {
            task
        }

        _tasks.add(newTask)
        _tasksFlow.value = _tasks.toList() // Отправляем копию
    }

    override suspend fun updateTask(task: ToDoTask) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            _tasks[index] = task
            _tasksFlow.value = _tasks.toList() // Отправляем копию
        }
    }

    override suspend fun setCompleted(task: ToDoTask, taskCompleted: Boolean) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            _tasks[index] = _tasks[index].copy(completed = taskCompleted)
            _tasksFlow.value = _tasks.toList() // Отправляем копию
        }
    }

    override suspend fun setFavourite(task: ToDoTask, taskFavourite: Boolean) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            _tasks[index] = _tasks[index].copy(favourite = taskFavourite)
            _tasksFlow.value = _tasks.toList() // Отправляем копию
        }
    }

    override suspend fun deleteTask(task: ToDoTask) {
        _tasks.removeAll { it.id == task.id }
        _tasksFlow.value = _tasks.toList() // Отправляем копию
    }
}


class SettingsStorage(
    private val settings: Settings
) {

    private val json = Json { encodeDefaults = true }
    private val KEY = "tasks"

    fun loadTasks(): List<ToDoTask> {
        val raw = settings.getStringOrNull(KEY) ?: return emptyList()
        return json.decodeFromString(raw)
    }

    fun saveTasks(tasks: List<ToDoTask>) {
        settings.putString(KEY, json.encodeToString(tasks))
    }
}


class PersistentStorage(
    private val settingsStorage: SettingsStorage
) : TaskStorage {
    private val _tasks = mutableStateListOf<ToDoTask>()

    init {
        _tasks.addAll(settingsStorage.loadTasks())
    }

    fun getTasks(): List<ToDoTask> = _tasks

    fun add(task: ToDoTask) {
        _tasks.add(task)
        persist()
    }

    fun update(task: ToDoTask) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            _tasks[index] = task
            persist()
        }
    }

    fun delete(task: ToDoTask) {
        _tasks.removeAll { it.id == task.id }
        persist()
    }

    private fun persist() {
        settingsStorage.saveTasks(_tasks)
    }

    private val json = Json { encodeDefaults = true }

    init {
        _tasks.addAll(settingsStorage.loadTasks())
    }

    override fun readAllTasks(): Flow<RequestState<List<ToDoTask>>> = flow {
        emit(RequestState.Success(_tasks.filter { !it.completed }))
    }

    override fun readCompletedTasks(): Flow<RequestState<List<ToDoTask>>> = flow {
        emit(RequestState.Success(_tasks.filter { it.completed }))
    }

    override suspend fun addTask(task: ToDoTask) {
        _tasks.add(task)
        persist()
    }

    override suspend fun updateTask(task: ToDoTask) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            _tasks[index] = task
            persist()
        }
    }

    override suspend fun deleteTask(task: ToDoTask) {
        _tasks.removeAll { it.id == task.id }
        persist()
    }

    override suspend fun setCompleted(task: ToDoTask, completed: Boolean) {
        updateTask(task.copy(completed = completed))
    }

    override suspend fun setFavourite(task: ToDoTask, favourite: Boolean) {
        updateTask(task.copy(favourite = favourite))
    }
}