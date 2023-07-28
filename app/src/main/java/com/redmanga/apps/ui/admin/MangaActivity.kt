package com.redmanga.apps.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmanga.apps.R
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.response.Kategori
import com.redmanga.apps.databinding.ActivityMangaBinding
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MangaActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private lateinit var adminViewModel: AdminViewModel
    private val factory: AdminViewModelFactory by instance()
    val kategoriList: MutableList<Kategori> = mutableListOf()
    lateinit var adapter: MangaAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMangaBinding = ActivityMangaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "List Manga"

        adminViewModel = ViewModelProvider(this, factory).get(AdminViewModel::class.java)

        getManga()
        getKategori()

        adminViewModel.kategori.observe(this, Observer {
            it?.let {
                kategoriList.clear()
                kategoriList.addAll(it)
            }
        })

        val mangaList: MutableList<Manga> = mutableListOf()
        adapter = MangaAdminAdapter(mangaList)
        binding.rvManga.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvManga.context,
            LinearLayoutManager.VERTICAL
        )
        binding.rvManga.addItemDecoration(dividerItemDecoration)
        binding.rvManga.setHasFixedSize(true)
        binding.rvManga.adapter = adapter

        adminViewModel.manga.observe(this, Observer {
            mangaList.clear()
            mangaList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        adapter.ItemClick(object : MangaAdminAdapter.OnItemClick {
            override fun onItemClicked(item: Manga?) {
                val intent = Intent(this@MangaActivity, ChapterActivity::class.java)
                intent.putExtra(ChapterActivity.EXTRA_DATA, item)
                startActivity(intent)
            }

            override fun onLongItemClicked(item: Manga?) {
                val fragment = FragmentBottomSheet(
                    kategoriList = kategoriList,
                    manga = item,
                    layout = 2,
                    type = 1
                )
                fragment.show(supportFragmentManager, fragment.tag)
                supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                    FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentViewDestroyed(fm, f)
                        getManga()
                        supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                    }
                }, false)
            }

            override fun onItemDeleteClicked(item: Manga?, posisi: Int) {
                deleteDialog(posisi, item)
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_add_data, menu)
        return true
    }

    private fun getKategori() = Coroutines.main {
        adminViewModel.getKategori()
    }

    private fun getManga() = Coroutines.main {
        adminViewModel.getManga()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.tambah) {
            val fragment = FragmentBottomSheet(kategoriList = kategoriList, layout = 2)
            fragment.show(supportFragmentManager, fragment.tag)
            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    getManga()
                    supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }, false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDialog(posisi: Int, item: Manga?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Apakah anda ingin menghapus manga ${item?.judul}?")
        builder.setPositiveButton("YA") { _, _ ->
            Coroutines.main {
                val result = adminViewModel.deleteManga(item!!.id_manga)
                showToast(this@MangaActivity, result.message)
                if (result.message == "berhasil_hapus_data") {
                    adapter.deleteItem(posisi)
                }
            }
        }
        builder.setNegativeButton("TIDAK") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
