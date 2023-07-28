package com.redmanga.apps.ui.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redmanga.apps.data.repositories.MangaRepository

class ReadMangaViewModelFactory(
    private val repository: MangaRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ReadMangaViewModel(repository) as T
    }
}