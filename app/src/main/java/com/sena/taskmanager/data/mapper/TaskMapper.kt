package com.sena.taskmanager.data.mapper

import com.sena.taskmanager.data.remote.model.TaskDocument
import com.sena.taskmanager.domain.model.Task

fun TaskDocument.toDomain(id: String): Task {
    return Task(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toDocument(): TaskDocument {
    return TaskDocument(
        ownerId = ownerId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

