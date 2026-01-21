package org.example.todo.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


@Serializable
data class ToDoTask(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val favourite: Boolean = false,
    val completed: Boolean = false
)

interface TaskStorage {
    fun readAllTasks(): Flow<RequestState<List<ToDoTask>>>
    fun readCompletedTasks(): Flow<RequestState<List<ToDoTask>>>

    suspend fun addTask(task: ToDoTask)
    suspend fun updateTask(task: ToDoTask)
    suspend fun deleteTask(task: ToDoTask)

    suspend fun setCompleted(task: ToDoTask, completed: Boolean)
    suspend fun setFavourite(task: ToDoTask, favourite: Boolean)
}