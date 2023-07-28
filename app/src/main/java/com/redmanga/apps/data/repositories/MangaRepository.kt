package com.redmanga.apps.data.repositories

import androidx.lifecycle.MutableLiveData
import com.redmanga.apps.data.db.AppDatabase
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.db.entities.Reader
import com.redmanga.apps.data.network.MyApiService
import com.redmanga.apps.data.network.SafeApiRequest
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.data.network.response.ResultLogin
import com.redmanga.apps.data.network.response.ResultPostSearching
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.loginRequest
import com.redmanga.apps.utils.tambahPencarian

class MangaRepository(
    private val api: MyApiService,
    private val db: AppDatabase
) : SafeApiRequest() {

    val manga = MutableLiveData<List<Manga>>()
    val chapter = MutableLiveData<List<Chapter>>()
    val read = MutableLiveData<List<Chapter>>()
    val allRead = MutableLiveData<List<Int>>()

    init {
        manga.observeForever {
            saveManga(it)
        }
    }

    suspend fun inpSearching(
        judul: String,
        penulis: String
    ): ResultPostSearching {
        return apiRequest {
            api.inpSearching(
                tambahPencarian(judul, penulis)
            )
        }
    }

    suspend fun getManga() = Coroutines.main {
        fetchManga()
    }

    fun getReader(id: Int) = Coroutines.io {
        allRead.postValue(db.getRaderDao().getReader(id))
    }

    suspend fun getChapter(id: Int) = Coroutines.main {
        fetchChapter(id)
    }

    suspend fun getRead(id: Int) = Coroutines.main {
        fetchRead(id)
    }

    private suspend fun fetchManga() {
        val response = apiRequest { api.getManga() }
        manga.postValue(response.results)
    }

    private suspend fun fetchChapter(id: Int) {
        val response = apiRequest { api.getChapterMangaByID(id) }
        chapter.postValue(response.results)
    }

    private suspend fun fetchRead(id: Int) {
        val response = apiRequest { api.getReadMangaByID(id) }
        read.postValue(response.results)
    }

    suspend fun fetchLogin(username: String, password: String): ResultLogin {
        return apiRequest { api.loginRequest(loginRequest(username, password)) }
    }

    fun saveReader(reader: Reader) = Coroutines.io {
        db.getRaderDao().saveReader(reader)
    }

    private fun saveManga(manga: List<Manga>) {
        Coroutines.io {
            db.getMangaDao().saveAllManga(manga)
        }
    }

}