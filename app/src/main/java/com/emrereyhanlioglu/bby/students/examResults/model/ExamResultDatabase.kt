package com.emrereyhanlioglu.bby.students.examResults.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ExamResult::class), version = 1, exportSchema = false)
abstract class ExamResultDatabase: RoomDatabase() {
    abstract fun examResultDao(): ExamResultDao

    companion object {
        @Volatile
        private var instance: ExamResultDatabase?=null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ExamResultDatabase::class.java,
            "examresultdatabase"
        ).build()
    }
}