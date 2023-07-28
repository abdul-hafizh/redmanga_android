package com.redmanga.apps.ui.read

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmanga.apps.R
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.databinding.ActivityReadMangaBinding
import com.redmanga.apps.utils.Coroutines
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ReadMangaActivity : AppCompatActivity(), KodeinAware {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        const val EXTRA_LIST = "EXTRA_LIST"
    }

    override val kodein by kodein()
    private lateinit var readMangaViewModel: ReadMangaViewModel
    private val factory: ReadMangaViewModelFactory by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityReadMangaBinding = ActivityReadMangaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        readMangaViewModel = ViewModelProvider(this, factory).get(ReadMangaViewModel::class.java)

        val chapter: Chapter? = intent?.getParcelableExtra(EXTRA_DATA)
        val chapterList: MutableList<Chapter>? = intent.getParcelableArrayListExtra(EXTRA_LIST)
        readMangaViewModel.chapter.postValue(chapter)

        supportActionBar?.title = resources.getString(R.string.chapter, chapter?.chapter.toString())

        println("Jumlah chapter ${chapterList?.size}")

        getRead()


        val readList: MutableList<Chapter> = mutableListOf()
        val adapter = ReadMangaAdapter(readList)
        binding.rvRead.layoutManager = LinearLayoutManager(this)
        binding.rvRead.setHasFixedSize(true)
        binding.rvRead.adapter = adapter

        readMangaViewModel.listReadChapter.observe(this, Observer {
            it?.let {
                readList.clear()
                readList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        adapter.ItemClick(object : ReadMangaAdapter.OnItemClick {
            override fun onItemClicked(item: Chapter?) {

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getRead() = Coroutines.main {
        readMangaViewModel.read.await()
    }
}
