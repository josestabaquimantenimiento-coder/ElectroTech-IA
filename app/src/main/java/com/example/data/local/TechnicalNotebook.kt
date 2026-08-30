package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technical_notebooks")
data class TechnicalNotebook(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val machineCode: String,
    val area: String,
    val description: String,
    val colorHex: String = "#0284C7",
    val iconName: String = "settings",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notebook_notes")
data class NotebookNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val notebookId: Long,
    val title: String,
    val content: String,
    val author: String = "Técnico de Planta",
    val timestamp: Long = System.currentTimeMillis()
)
