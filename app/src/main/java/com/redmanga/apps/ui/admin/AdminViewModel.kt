package com.redmanga.apps.ui.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.response.*
import com.redmanga.apps.data.repositories.AdminRepository
import okhttp3.RequestBody

class AdminViewModel(
    repository: AdminRepository
) : ViewModel() {
    private val repo = repository
    var kategori = MutableLiveData<List<Kategori>>()
    var manga = MutableLiveData<List<Manga>>()
    var chapter = MutableLiveData<List<Chapter>>()
    var komik = MutableLiveData<List<Chapter>>()

    suspend fun getKategori() {
        kategori.postValue(repo.getKategori())
    }

    suspend fun addKategori(nama: String): ResultPostKategori {
        return repo.addKategori(nama)
    }

    suspend fun updateKategori(id: Int, nama: String): ResultPostKategori {
        return repo.updateKategori(id.toString(), nama)
    }

    suspend fun deleteKategori(id: Int): ResultPostKategori {
        return repo.deleteKategori(id)
    }

    suspend fun getManga() {
        manga.postValue(repo.getManga())
    }

    suspend fun addManga(
        manga: RequestBody
    ): ResultPostManga {
        return repo.addManga(manga)
    }

    suspend fun updateManga(
        id: String,
        id_kategori: String,
        judul: String,
        tanggal: String,
        deskripsi: String,
        penulis: String,
        status: String
    ): ResultPostManga {
        return repo.updateManga(id, id_kategori, judul, tanggal, deskripsi, penulis, status)
    }

    suspend fun deleteManga(id: Int): ResultPostManga {
        return repo.deleteManga(id)
    }

    suspend fun getChapter(id_manga: String) {
        chapter.postValue(repo.getChapter(id_manga))
    }

    suspend fun addChapter(id_manga:String,chapter: String,judul: String,tanggal: String): ResultPostChapter {
        return repo.addChapter(id_manga,chapter,judul,tanggal)
    }

    suspend fun updateChapter(id_chapter: String, id_manga: String,chapter: String,judul: String): ResultPostChapter {
        return repo.updateChapter(id_chapter,id_manga,chapter,judul)
    }

    suspend fun deleteChapter(id: Int): ResultPostChapter {
        return repo.deleteChapter(id)
    }

    suspend fun getKomik(id_chapter:Int) {
        komik.postValue(repo.getKomik(id_chapter))
    }

    suspend fun addKomik(komik: RequestBody): ResultPostKomik {
        return repo.addKomik(komik)
    }

    suspend fun deleteKomik(id: Int): ResultPostKomik {
        return repo.deleteKomik(id)
    }
}