package com.redmanga.apps.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Manga(
    val cover: String = "",
    val deskripsi: String = "",
    @PrimaryKey(autoGenerate = false)
    val id_manga: Int = 0,
    val id_kategori: Int = 0,
    val judul: String = "",
    val nama_kategori: String = "",
    val pengunjung: Int = 0,
    val penulis: String = "",
    val status: String = "",
    val tgl_release: String = ""
) : Parcelable