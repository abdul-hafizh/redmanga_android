package com.redmanga.apps.data.network.response

data class ResultPostSearching(
    val code: Int = 0,
    val message: String = "",
    val results: ResultsSearchig = ResultsSearchig()
)

data class ResultsSearchig(
    val judul: String = "",
    val penulis: String = ""
)