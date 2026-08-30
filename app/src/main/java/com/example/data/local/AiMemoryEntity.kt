package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_memories")
data class AiMemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "Máquina / Cuadro", "Regla de Diagnóstico", "Preferencia de Taller", "Parámetro Especial", "Fallo Frecuente"
    val content: String,
    val source: String = "Definido por Usuario", // "Definido por Usuario", "Aprendido en Reparación", "Web Research", "Visión IA"
    val isEnabled: Boolean = true,
    val timestamp: Long = System.currentTimeMillis(),
    val notebookId: Long? = null
)
