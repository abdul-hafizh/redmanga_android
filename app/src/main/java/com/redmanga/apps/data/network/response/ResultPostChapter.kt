package com.redmanga.apps.data.network.response

data class ResultPostChapter(
    val code: Int = 0,
    val message: String = "",
    val results: ResultsChapter = ResultsChapter()
)

data class ResultsChapter(
    val chapter: String = "",
    val id_manga: String = "",
    val judul_chapter: String = "",
    val tanggal: String = ""
)