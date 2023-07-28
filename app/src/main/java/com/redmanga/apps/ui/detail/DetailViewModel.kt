package com.redmanga.apps.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.db.entities.Reader
import com.redmanga.apps.data.repositories.MangaRepository
import com.redmanga.apps.utils.lazyDeferred


class DetailViewModel(
    repository: MangaRepository
) : ViewModel() {
    val repo = repository
    var data = MutableLiveData<Manga>()
    var read = repo.allRead

    val chapter by lazyDeferred {
        repository.getChapter(data.value!!.id_manga)
    }

    val reader by lazyDeferred {
        repository.getReader(data.value!!.id_manga)
    }

    val listChapter = repository.chapter

    fun saveRead(reader: Reader) {
        repo.saveReader(reader)
    }
}