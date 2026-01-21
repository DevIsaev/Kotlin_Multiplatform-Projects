package org.example.todo.domain

sealed class TaskAction {
    data class Add(var task: ToDoTask): TaskAction()
    data class Update(var task: ToDoTask): TaskAction()
    data class Delete(var task: ToDoTask): TaskAction()
    data class SetCompleted(var task: ToDoTask,var completed:Boolean): TaskAction()
    data class SetFavourite(var task: ToDoTask,var completed:Boolean): TaskAction()
}