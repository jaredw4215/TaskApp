package com.example.taskapp


data class TaskModel(
    var id: Int? = null,
    var title: String = "",
    var description: String = "",
    var dueDate: String = "",
    var isCompleted: Boolean = false,
    var priority: Int = 3,
    var category: String = "",
)
