package com.redmanga.apps.data.network.response

data class ResultPostManga(
    val code: Int = 0,
    val message: String = "",
    val results: ResultsManga = ResultsManga()
)

data class ResultsManga(
    val cover: String = "",
    val deskripsi: String = "",
    val id_kategori: String = "",
    val judul: String = "",
    val penulis: String = "",
    val status: String = "",
    val tgl_release: String = ""
)