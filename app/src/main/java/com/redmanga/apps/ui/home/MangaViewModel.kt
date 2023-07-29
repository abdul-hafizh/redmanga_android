package com.redmanga.apps.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.redmanga.apps.data.db.AppDatabase
import com.redmanga.apps.data.db.MangaDao
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.response.ResultLogin
import com.redmanga.apps.data.repositories.MangaRepository
import com.redmanga.apps.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

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

    val s = MutableLiveData<PagedList<Manga>>()

    fun pencarianManga(judul: String, kategori: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val dataSourceFactory = dao.getResManga(judul, kategori)
            val config = PagedList.Config.Builder().setPageSize(10).build()

            val result = PagedList.Builder(dataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor()) // Untuk memastikan operasi data berjalan di thread terpisah
                .setNotifyExecutor(MainThreadExecutor()) // Pastikan notifikasi perubahan berjalan di thread utama
                .build()

            // Kirim hasil ke thread utama
            withContext(Dispatchers.Main) {
                s.value = result
            }
        }
    }



}

