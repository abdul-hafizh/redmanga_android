package com.redmanga.apps.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


fun loginRequest(username: String, password: String): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["username"] = username
    params["password"] = password
    return params
}

fun requestBody(data: String): RequestBody {
    return data.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun tambahKategori(nama: String): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["nama_kategori"] = nama
    return params
}

fun tambahPencarian(judul: String, penulis: String): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["judul"] = judul
    params["penulis"] = penulis
    return params
}

fun ubahKategori(id: String, nama: String): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["id_kategori"] = id
    params["nama_kategori"] = nama
    return params
}

fun ubahManga(
    id_manga: String,
    id_kategori: String,
    judul: String,
    tgl_release: String,
    deskripsi: String,
    penulis: String,
    status: String
): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["id_manga"] = id_manga
    params["id_kategori"] = id_kategori
    params["judul"] = judul
    params["tgl_release"] = tgl_release
    params["deskripsi"] = deskripsi
    params["penulis"] = penulis
    params["status"] = status
    return params
}

fun tambahChapter(
    id_manga: String,
    chapter: String,
    judul_chapter: String,
    tanggal: String
): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["id_manga"] = id_manga
    params["chapter"] = chapter
    params["judul_chapter"] = judul_chapter
    params["tanggal"] = tanggal
    return params
}

fun ubahChapter(
    id_chapter: String,
    id_manga: String,
    chapter: String,
    judul_chapter: String
): MutableMap<String, String> {
    val params: MutableMap<String, String> = HashMap()
    params["id_chapter"] = id_chapter
    params["id_manga"] = id_manga
    params["chapter"] = chapter
    params["judul_chapter"] = judul_chapter
    return params
}

