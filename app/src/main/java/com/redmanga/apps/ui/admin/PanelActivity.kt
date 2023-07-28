package com.redmanga.apps.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.redmanga.apps.R
import com.redmanga.apps.databinding.ActivityPanelBinding
import com.redmanga.apps.utils.Coroutines
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class PanelActivity : AppCompatActivity(),KodeinAware {

    override val kodein by kodein()

    private lateinit var adminViewModel: AdminViewModel
    private val factory: AdminViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPanelBinding = ActivityPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dashboard Admin"

        binding.manga.setOnClickListener { startActivity(Intent(this@PanelActivity,MangaActivity::class.java)) }
        binding.kategori.setOnClickListener { startActivity(Intent(this@PanelActivity,KategoriActivity::class.java)) }

        adminViewModel = ViewModelProvider(this, factory).get(AdminViewModel::class.java)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
