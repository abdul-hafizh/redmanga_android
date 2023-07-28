package com.redmanga.apps.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redmanga.apps.data.repositories.MangaRepository

class MangaViewModelFactory(
    private val repository: MangaRepository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MangaViewModel(repository,context) as T
    }
}