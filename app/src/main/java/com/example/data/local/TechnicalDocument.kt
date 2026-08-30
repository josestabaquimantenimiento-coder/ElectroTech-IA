package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technical_documents")
data class TechnicalDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val manufacturer: String,
    val model: String,
    val category: String, // "Variador VFD", "Protección Motor", "Bomba Hidráulica", "Autómata PLC", "Normativa"
    val pageReference: String,
    val contentSnippet: String,
    val expectedValues: String,
    val diagnosticProcedure: String,
    val notebookId: Long? = null
)
