package com.redmanga.apps.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redmanga.apps.data.db.entities.Reader

@Dao
interface ReaderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReader(reader: Reader)

    @Query("SELECT id_chapter FROM Reader WHERE id_manga = :id ORDER BY id_chapter ASC")
    fun getReader(id: Int): List<Int>
}