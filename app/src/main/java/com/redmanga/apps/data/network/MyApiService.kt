package com.redmanga.apps.data.network

import com.redmanga.apps.data.network.response.*
import com.themoviekotlin.apps.data.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface MyApiService {
    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okkHttpclient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .addInterceptor(networkConnectionInterceptor)
                .build()


            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://redmangaonline.000webhostapp.com/api/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApiService::class.java)
        }
    }

    //PENGUNJUNG
    @GET("manga")
    suspend fun getManga(): Response<ResultManga>

    @GET("chapter_by_manga/{id}")
    suspend fun getChapterMangaByID(@Path("id") id: Int): Response<ResultChapter>

    @GET("detail/{id}")
    suspend fun getReadMangaByID(@Path("id") id: Int): Response<ResultChapter>

    @POST("manga_filter")
    suspend fun inpSearching(
        @Body params: MutableMap<String, String>
    ): Response<ResultPostSearching>

    //ADMIN
    @POST("login")
    suspend fun loginRequest(
        @Body params: MutableMap<String, String>
    ): Response<ResultLogin>

    @GET("a_kategori")
    suspend fun getKategori(@Query("token") token: String): Response<ResultKategori>

    @POST("a_kategori/insert")
    suspend fun addKategori(
        @Body params: MutableMap<String, String>,
        @Query("token") token: String
    ): Response<ResultPostKategori>

    @PATCH("a_kategori/update")
    suspend fun updateKategori(
        @Body params: MutableMap<String, String>,
        @Query("token") token: String
    ): Response<ResultPostKategori>

    @DELETE("a_kategori/delete/{id}")
    suspend fun deleteKategori(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<ResultPostKategori>

    @GET("a_manga")
    suspend fun getManga(@Query("token") token: String): Response<ResultManga>

    @POST("a_manga/insert")
    suspend fun addManga(
        @Query("token") token: String,
        @Body manga: RequestBody
    ): Response<ResultPostManga>

    @PATCH("a_manga/update")
    suspend fun updateManga(
        @Body params: MutableMap<String, String>,
        @Query("token") token: String
    ): Response<ResultPostManga>

    @DELETE("a_manga/delete/{id}")
    suspend fun deleteManga(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<ResultPostManga>

    @GET("a_chapter_by_manga/{id}")
    suspend fun getChapter(@Path("id") id_manga: String): Response<ResultChapter>

    @POST("a_chapter/insert")
    suspend fun addChapter(
        @Body params: MutableMap<String, String>,
        @Query("token") token: String
    ): Response<ResultPostChapter>

    @PATCH("a_chapter/update")
    suspend fun updateChapter(
        @Body params: MutableMap<String, String>,
        @Query("token") token: String
    ): Response<ResultPostChapter>

    @DELETE("a_chapter/delete/{id}")
    suspend fun deleteChapter(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<ResultPostChapter>

    @GET("a_detail/{id}")
    suspend fun getKomik(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<ResultChapter>

    @POST("a_detail/upload")
    suspend fun addKomik(
        @Body komik: RequestBody,
        @Query("token") token: String
    ): Response<ResultPostKomik>

    @DELETE("a_detail/delete/{id}")
    suspend fun deleteKomik(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<ResultPostKomik>
}