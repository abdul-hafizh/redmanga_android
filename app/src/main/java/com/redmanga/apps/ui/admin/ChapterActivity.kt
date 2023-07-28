package com.redmanga.apps.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmanga.apps.R
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.databinding.ActivityChapterBinding
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.kodein

class ChapterActivity : AppCompatActivity(), KodeinAware {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override val kodein by kodein()

    private lateinit var adminViewModel: AdminViewModel
    private val factory: AdminViewModelFactory by instance()
    lateinit var adapter: ChapterAdminAdapter
    lateinit var manga:Manga
    lateinit var idManga:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityChapterBinding = ActivityChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manga = intent.getParcelableExtra(EXTRA_DATA)!!

        setSupportActionBar(binding.topBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = manga.judul

        idManga = manga.id_manga.toString()

        adminViewModel = ViewModelProvider(this, factory).get(AdminViewModel::class.java)

        getChapter(idManga)

        val chapterList: MutableList<Chapter> = mutableListOf()
        adapter = ChapterAdminAdapter(chapterList)
        binding.rvChapter.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvChapter.context,
            LinearLayoutManager.VERTICAL
        )
        binding.rvChapter.addItemDecoration(dividerItemDecoration)
        binding.rvChapter.setHasFixedSize(true)
        binding.rvChapter.adapter = adapter

        adminViewModel.chapter.observe(this, Observer {
            chapterList.clear()
            chapterList.addAll(it)
            adapter.notifyDataSetChanged()
            if(it.isEmpty()){
                Toast.makeText(this,"Data Kosong",Toast.LENGTH_SHORT).show()
            }
        })

        adapter.ItemClick(object : ChapterAdminAdapter.OnItemClick {
            override fun onItemClicked(item: Chapter?) {
                val intent = Intent(this@ChapterActivity, KomikActivity::class.java)
                intent.putExtra(KomikActivity.EXTRA_DATA, item)
                startActivity(intent)
            }

            override fun onLongItemClicked(item: Chapter?) {
                val fragment = FragmentBottomSheet(
                    manga = manga,
                    dataChapter = item,
                    layout = 3,
                    type = 1
                )
                fragment.show(supportFragmentManager, fragment.tag)
                supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                    FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentViewDestroyed(fm, f)
                        getChapter(idManga)
                        supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                    }
                }, false)
            }

            override fun onItemDeleteClicked(item: Chapter?, posisi: Int) {
                deleteDialog(posisi, item)
            }

        })

    }

    private fun getChapter(id: String) = Coroutines.main {
        adminViewModel.getChapter(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_add_data, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.tambah) {
            val fragment = FragmentBottomSheet(layout = 3, manga = manga)
            fragment.show(supportFragmentManager, fragment.tag)
            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    getChapter(idManga)
                    supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }, false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDialog(posisi: Int, item: Chapter?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Apakah anda ingin menghapus chapter ${item?.judul_chapter}?")
        builder.setPositiveButton("YA") { _, _ ->
            Coroutines.main {
                val result = adminViewModel.deleteChapter(item!!.id_chapter)
                showToast(this@ChapterActivity, result.message)
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
