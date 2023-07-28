package com.redmanga.apps.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reader(
    @PrimaryKey(autoGenerate = false)
    val id_chapter: Int = 0,
    val id_manga: Int = 0,
    val status: Boolean = false
)