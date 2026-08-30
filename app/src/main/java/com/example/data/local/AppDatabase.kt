package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        InterventionEntity::class,
        TechnicalDocument::class,
        AiMemoryEntity::class,
        TechnicalNotebook::class,
        NotebookNote::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interventionDao(): InterventionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "electrotech_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.interventionDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.interventionDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: InterventionDao) {
                if (dao.getNotebookCount() == 0) {
                    dao.insertNotebooks(PreloadData.sampleNotebooks)
                }
                if (dao.getNoteCount() == 0) {
                    dao.insertNotes(PreloadData.sampleNotes)
                }
                if (dao.getDocumentCount() == 0) {
                    dao.insertDocuments(PreloadData.sampleDocuments)
                    PreloadData.sampleInterventions.forEach {
                        dao.insertIntervention(it)
                    }
                }
                if (dao.getMemoryCount() == 0) {
                    dao.insertMemories(PreloadData.sampleMemories)
                }
            }
        }
    }
}
