package com.redmanga.apps.data.network.response

data class ResultKategori(
    val message: String = "",
    val results: List<Kategori> = listOf()
)

data class Kategori(
    val created_at: String = "",
    val id_kategori: Int = 0,
    val nama_kategori: String = "",
    val updated_at: String = ""
)
