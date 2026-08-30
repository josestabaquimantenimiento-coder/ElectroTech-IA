package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InterventionDao {
    @Query("SELECT * FROM interventions ORDER BY timestamp DESC")
    fun getAllInterventions(): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE equipmentName LIKE '%' || :query || '%' OR cabinetCode LIKE '%' || :query || '%' OR failureDescription LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchInterventions(query: String): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE status = :status ORDER BY timestamp DESC")
    fun getInterventionsByStatus(status: String): Flow<List<InterventionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntervention(intervention: InterventionEntity): Long

    @Update
    suspend fun updateIntervention(intervention: InterventionEntity)

    @Delete
    suspend fun deleteIntervention(intervention: InterventionEntity)

    @Query("DELETE FROM interventions WHERE id = :id")
    suspend fun deleteById(id: Long)

    // Technical Documents
    @Query("SELECT * FROM technical_documents ORDER BY manufacturer, title ASC")
    fun getAllDocuments(): Flow<List<TechnicalDocument>>

    @Query("SELECT * FROM technical_documents WHERE title LIKE '%' || :query || '%' OR contentSnippet LIKE '%' || :query || '%' OR diagnosticProcedure LIKE '%' || :query || '%' OR manufacturer LIKE '%' || :query || '%'")
    fun searchDocuments(query: String): Flow<List<TechnicalDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(docs: List<TechnicalDocument>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: TechnicalDocument): Long

    @Update
    suspend fun updateDocument(doc: TechnicalDocument)

    @Delete
    suspend fun deleteDocument(doc: TechnicalDocument)

    @Query("DELETE FROM technical_documents WHERE id = :id")
    suspend fun deleteDocumentById(id: Long)

    @Query("SELECT COUNT(*) FROM technical_documents")
    suspend fun getDocumentCount(): Int

    // AI Memories
    @Query("SELECT * FROM ai_memories ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<AiMemoryEntity>>

    @Query("SELECT * FROM ai_memories WHERE isEnabled = 1 ORDER BY timestamp DESC")
    fun getActiveMemories(): Flow<List<AiMemoryEntity>>

    @Query("SELECT * FROM ai_memories WHERE isEnabled = 1")
    suspend fun getActiveMemoriesSync(): List<AiMemoryEntity>

    @Query("SELECT * FROM ai_memories WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchMemories(query: String): Flow<List<AiMemoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: AiMemoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemories(memories: List<AiMemoryEntity>)

    @Update
    suspend fun updateMemory(memory: AiMemoryEntity)

    @Delete
    suspend fun deleteMemory(memory: AiMemoryEntity)

    @Query("DELETE FROM ai_memories WHERE id = :id")
    suspend fun deleteMemoryById(id: Long)

    @Query("UPDATE ai_memories SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun toggleMemoryStatus(id: Long, isEnabled: Boolean)

    @Query("SELECT COUNT(*) FROM ai_memories")
    suspend fun getMemoryCount(): Int

    // Notebooks (Máquinas / Sistemas)
    @Query("SELECT * FROM technical_notebooks ORDER BY name ASC")
    fun getAllNotebooks(): Flow<List<TechnicalNotebook>>

    @Query("SELECT * FROM technical_notebooks WHERE id = :id")
    suspend fun getNotebookById(id: Long): TechnicalNotebook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotebook(notebook: TechnicalNotebook): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotebooks(notebooks: List<TechnicalNotebook>)

    @Update
    suspend fun updateNotebook(notebook: TechnicalNotebook)

    @Delete
    suspend fun deleteNotebook(notebook: TechnicalNotebook)

    @Query("DELETE FROM technical_notebooks WHERE id = :id")
    suspend fun deleteNotebookById(id: Long)

    @Query("SELECT COUNT(*) FROM technical_notebooks")
    suspend fun getNotebookCount(): Int

    // Notebook Notes
    @Query("SELECT * FROM notebook_notes WHERE notebookId = :notebookId ORDER BY timestamp DESC")
    fun getNotesForNotebook(notebookId: Long): Flow<List<NotebookNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotebookNote): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NotebookNote>)

    @Delete
    suspend fun deleteNote(note: NotebookNote)

    @Query("DELETE FROM notebook_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("SELECT COUNT(*) FROM notebook_notes")
    suspend fun getNoteCount(): Int

    // Cross queries by Notebook
    @Query("SELECT * FROM technical_documents WHERE notebookId = :notebookId ORDER BY title ASC")
    fun getDocumentsByNotebook(notebookId: Long): Flow<List<TechnicalDocument>>

    @Query("SELECT * FROM ai_memories WHERE notebookId = :notebookId ORDER BY timestamp DESC")
    fun getMemoriesByNotebook(notebookId: Long): Flow<List<AiMemoryEntity>>

    @Query("SELECT * FROM interventions WHERE notebookId = :notebookId ORDER BY timestamp DESC")
    fun getInterventionsByNotebook(notebookId: Long): Flow<List<InterventionEntity>>
}
