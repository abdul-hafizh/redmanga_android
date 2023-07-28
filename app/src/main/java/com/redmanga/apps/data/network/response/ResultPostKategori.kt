package com.redmanga.apps.data.network.response

data class ResultPostKategori(
    val code: Int = 0,
    val message: String = "",
    val results: ResultsKategori = ResultsKategori()
)

data class ResultsKategori(
    val nama_kategori: String = ""
)