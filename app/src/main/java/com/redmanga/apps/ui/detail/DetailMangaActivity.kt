package com.redmanga.apps.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.databinding.ActivityDetailMangaBinding
import com.redmanga.apps.ui.ViewPagerAdapter
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.displayImage
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class DetailMangaActivity : AppCompatActivity(), KodeinAware {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override val kodein by kodein()
    private lateinit var detailViewModel: DetailViewModel
    private val factory: DetailViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailMangaBinding = ActivityDetailMangaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val data: Manga? = intent?.getParcelableExtra(EXTRA_DATA)

        detailViewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        detailViewModel.data.postValue(data)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = data?.judul

        displayImage(this, binding.background, data!!.cover)

        val viewPager = binding.includeContent.viewpager
        setupViewPager(viewPager)

        val tabLayout = binding.includeContent.tabs
        tabLayout.setupWithViewPager(viewPager)
        getChapter()
        getReader()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getReader() = Coroutines.main {
        detailViewModel.reader.await()
    }

    private fun getChapter() = Coroutines.main {
        detailViewModel.chapter.await()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DetailFragment(), "Detail")
        adapter.addFragment(ChapterFragment(), "Chapter")
        viewPager.adapter = adapter
    }
}
