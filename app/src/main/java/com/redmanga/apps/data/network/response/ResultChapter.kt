package com.redmanga.apps.data.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ResultChapter(
    val message: String = "",
    val results: List<Chapter> = listOf()
)

@Parcelize
data class Chapter(
    val id_chapter: Int = 0,
    val chapter: Int = 0,
    val id_detail:Int = 0,
    val judul: String = "",
    val judul_chapter: String = "",
    val nama_kategori: String = "",
    val tanggal: String = "",
    val gambar:String = ""
) : Parcelable