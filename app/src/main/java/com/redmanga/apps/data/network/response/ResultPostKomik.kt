package com.redmanga.apps.data.network.response

data class ResultPostKomik(
    val code: Int = 0,
    val message: String = "",
    val results: ResultsKomik = ResultsKomik()
)

data class ResultsKomik(
    val gambar: String = ""
)