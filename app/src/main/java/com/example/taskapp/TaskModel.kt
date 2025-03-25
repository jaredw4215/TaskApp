package com.example.taskapp

import java.sql.Date
import java.util.UUID

data class TaskModel(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var dueDate: String? = null,
    var isCompleted: Boolean = false,
    var priority: String? = null,
    var category: String? = null,
)
