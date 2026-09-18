package com.sena.taskmanager.domain.model

data class TaskDraft(
    val id: Int = 0,
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val savedAt: Long = 0L
)