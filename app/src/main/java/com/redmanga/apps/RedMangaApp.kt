package com.redmanga.apps

import android.app.Application
import com.redmanga.apps.data.db.AppDatabase
import com.redmanga.apps.data.network.MyApiService
import com.redmanga.apps.data.preference.PreferenceProvider
import com.redmanga.apps.data.repositories.AdminRepository
import com.redmanga.apps.data.repositories.MangaRepository
import com.redmanga.apps.ui.admin.AdminViewModelFactory
import com.redmanga.apps.ui.detail.DetailViewModelFactory
import com.redmanga.apps.ui.home.MangaViewModelFactory
import com.redmanga.apps.ui.read.ReadMangaViewModelFactory
import com.redmanga.apps.utils.Coroutines
import com.themoviekotlin.apps.data.network.NetworkConnectionInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class RedMangaApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@RedMangaApp))

        bind() from singleton { MyApiService(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { MangaRepository(instance(), instance()) }
        bind() from singleton { AdminRepository(instance(), instance()) }

        bind() from provider { MangaViewModelFactory(instance(), instance()) }
        bind() from provider { DetailViewModelFactory(instance()) }
        bind() from provider { ReadMangaViewModelFactory(instance()) }
        bind() from provider { AdminViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        Coroutines.context.postValue(applicationContext)
    }
}