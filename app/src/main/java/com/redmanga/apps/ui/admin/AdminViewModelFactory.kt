package com.redmanga.apps.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redmanga.apps.data.repositories.AdminRepository

class AdminViewModelFactory (
    private val repository: AdminRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AdminViewModel(repository) as T
    }
}