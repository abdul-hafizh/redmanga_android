package com.redmanga.apps.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.redmanga.apps.data.db.AppDatabase
import com.redmanga.apps.data.db.MangaDao
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.response.ResultLogin
import com.redmanga.apps.data.repositories.MangaRepository
import com.redmanga.apps.utils.lazyDeferred

class MangaViewModel(
    repository: MangaRepository,
    context: Context
) : ViewModel() {
    var dao: MangaDao = AppDatabase.invoke(context).getMangaDao()
    val repo = repository

    val manga by lazyDeferred {
        repository.getManga()
    }

    val pagedListLiveDataNewManga: LiveData<PagedList<Manga>> by lazy {
        val dataSourceFactory = dao.getNewManga()
        val config = PagedList.Config.Builder().setPageSize(10).build()
        LivePagedListBuilder(dataSourceFactory, config).build()
    }

    val pagedListLiveDataMostViewManga: LiveData<PagedList<Manga>> by lazy {
        val dataSourceFactory = dao.getMostViewManga()
        val config = PagedList.Config.Builder().setPageSize(10).build()
        LivePagedListBuilder(dataSourceFactory, config).build()
    }
    val pagedListLiveDataLastRealeaseManga: LiveData<PagedList<Manga>> by lazy {
        val dataSourceFactory = dao.getLastestRealeasManga()
        val config = PagedList.Config.Builder().setPageSize(10).build()
        LivePagedListBuilder(dataSourceFactory, config).build()
    }

    suspend fun loginRequest(username: String, password: String): ResultLogin {
        return repo.fetchLogin(username, password)
    }

    fun deleteAllManga(): Int {
        return dao.deleteAllManga()
    }

}

