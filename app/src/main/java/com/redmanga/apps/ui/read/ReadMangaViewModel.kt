package com.redmanga.apps.ui.read

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.data.repositories.MangaRepository
import com.redmanga.apps.utils.lazyDeferred

class ReadMangaViewModel(
    repository: MangaRepository
): ViewModel() {

    var chapter = MutableLiveData<Chapter>()

    val read by lazyDeferred {
        repository.getRead(chapter.value!!.id_chapter)
    }

    val listReadChapter = repository.read
}