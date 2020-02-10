package com.emrereyhanlioglu.bby.students.examResults.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExamResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg results: ExamResult)

    @Query("DELETE FROM ExamResult")
    suspend fun deleteAll()

    @Query("SELECT * FROM examresult")
    suspend fun getAllResults() : List<ExamResult>
}