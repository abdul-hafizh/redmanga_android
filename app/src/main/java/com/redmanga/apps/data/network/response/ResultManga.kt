package com.redmanga.apps.data.network.response

import com.redmanga.apps.data.db.entities.Manga

data class ResultManga(
    val message: String = "",
    val results: List<Manga> = listOf()
)