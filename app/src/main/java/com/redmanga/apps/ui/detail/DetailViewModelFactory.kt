package com.redmanga.apps.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redmanga.apps.data.repositories.MangaRepository

class DetailViewModelFactory(
    private val repository: MangaRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DetailViewModel(repository) as T
    }
}