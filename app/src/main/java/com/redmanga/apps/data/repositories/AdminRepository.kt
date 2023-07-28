package com.redmanga.apps.data.repositories

import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.MyApiService
import com.redmanga.apps.data.network.SafeApiRequest
import com.redmanga.apps.data.network.response.*
import com.redmanga.apps.data.preference.PreferenceProvider
import com.redmanga.apps.utils.*
import okhttp3.RequestBody

class AdminRepository(
    private val api: MyApiService,
    private val preferenceProvider: PreferenceProvider
) : SafeApiRequest() {

    suspend fun getKategori(): List<Kategori> {
        val response = apiRequest { api.getKategori(preferenceProvider.getToken()!!) }
        return response.results
    }

    suspend fun addKategori(nama: String): ResultPostKategori {
        return apiRequest { api.addKategori(tambahKategori(nama), preferenceProvider.getToken()!!) }
    }

    suspend fun updateKategori(id: String, nama: String): ResultPostKategori {
        return apiRequest {
            api.updateKategori(
                ubahKategori(id, nama),
                preferenceProvider.getToken()!!
            )
        }
    }

    suspend fun deleteKategori(id: Int): ResultPostKategori {
        return apiRequest { api.deleteKategori(id, preferenceProvider.getToken()!!) }
    }

    suspend fun getManga(): List<Manga> {
        val response = apiRequest { api.getManga(preferenceProvider.getToken()!!) }
        return response.results
    }

    suspend fun addManga(
        manga: RequestBody
    ): ResultPostManga {
        return apiRequest {
            api.addManga(
                preferenceProvider.getToken()!!,
                manga
            )
        }
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
        return apiRequest {
            api.updateManga(
                ubahManga(
                    id,
                    id_kategori,
                    judul,
                    tanggal,
                    deskripsi,
                    penulis,
                    status
                ), preferenceProvider.getToken()!!
            )
        }
    }

    suspend fun deleteManga(id: Int): ResultPostManga {
        return apiRequest { api.deleteManga(id, preferenceProvider.getToken()!!) }
    }

    suspend fun getChapter(id_manga: String): List<Chapter> {
        val response = apiRequest { api.getChapter(id_manga) }
        return response.results
    }

    suspend fun addChapter(
        id_manga: String,
        chapter: String,
        judul: String,
        tanggal: String
    ): ResultPostChapter {
        return apiRequest {
            api.addChapter(
                tambahChapter(id_manga, chapter, judul, tanggal),
                preferenceProvider.getToken()!!
            )
        }
    }

    suspend fun updateChapter(
        id_chapter: String,
        id_manga: String,
        chapter: String,
        judul: String
    ): ResultPostChapter {
        return apiRequest {
            api.updateChapter(
                ubahChapter(id_chapter, id_manga, chapter, judul),
                preferenceProvider.getToken()!!
            )
        }
    }

    suspend fun deleteChapter(id: Int): ResultPostChapter {
        return apiRequest { api.deleteChapter(id, preferenceProvider.getToken()!!) }
    }

    suspend fun getKomik(id_chapter: Int): List<Chapter> {
        val response = apiRequest { api.getKomik(id_chapter, preferenceProvider.getToken()!!) }
        return response.results
    }

    suspend fun addKomik(komik: RequestBody): ResultPostKomik {
        return apiRequest { api.addKomik(komik, preferenceProvider.getToken()!!) }
    }

    suspend fun deleteKomik(id: Int): ResultPostKomik {
        return apiRequest { api.deleteKomik(id, preferenceProvider.getToken()!!) }
    }

}