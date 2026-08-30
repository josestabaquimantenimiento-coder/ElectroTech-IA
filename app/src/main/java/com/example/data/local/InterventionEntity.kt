package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interventions")
data class InterventionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipmentName: String,
    val cabinetCode: String,
    val failureDescription: String,
    val rootCause: String,
    val actionsTaken: String,
    val partsReplaced: String = "",
    val status: String = "Resuelto", // "Resuelto", "En Diagnóstico", "Pendiente Repuesto"
    val timestamp: Long = System.currentTimeMillis(),
    val photoUri: String? = null,
    val schematicRef: String? = null,
    val technicalNotes: String = "",
    val notebookId: Long? = null
)
