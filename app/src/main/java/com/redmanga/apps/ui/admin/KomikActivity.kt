package com.redmanga.apps.ui.admin

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
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.databinding.ActivityKomikBinding
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class KomikActivity : AppCompatActivity(),KodeinAware {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override val kodein by kodein()

    private lateinit var adminViewModel: AdminViewModel
    private val factory: AdminViewModelFactory by instance()
    lateinit var adapter: KomikAdminAdapter
    lateinit var chapter: Chapter
    lateinit var idChapter:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityKomikBinding = ActivityKomikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chapter = intent.getParcelableExtra(ChapterActivity.EXTRA_DATA)!!

        setSupportActionBar(binding.topBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = chapter.judul_chapter

        idChapter = chapter.id_chapter.toString()

        adminViewModel = ViewModelProvider(this, factory).get(AdminViewModel::class.java)

        getKomik(idChapter)

        val komikList: MutableList<Chapter> = mutableListOf()
        adapter = KomikAdminAdapter(komikList)
        binding.rvKomik.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvKomik.context,
            LinearLayoutManager.VERTICAL
        )
        binding.rvKomik.addItemDecoration(dividerItemDecoration)
        binding.rvKomik.setHasFixedSize(true)
        binding.rvKomik.adapter = adapter

        adminViewModel.komik.observe(this, Observer {
            komikList.clear()
            komikList.addAll(it)
            adapter.notifyDataSetChanged()
            if(it.isEmpty()){
                Toast.makeText(this,"Data Kosong", Toast.LENGTH_SHORT).show()
            }
        })

        adapter.ItemClick(object : KomikAdminAdapter.OnItemClick {
            override fun onItemClicked(item: Chapter?) {
//                val intent = Intent(this@ChapterActivity, KomikActivity::class.java)
//                intent.putExtra(KomikActivity.EXTRA_DATA, item)
//                startActivity(intent)
            }

            override fun onLongItemClicked(item: Chapter?) {
//                val fragment = FragmentBottomSheet(
//                    dataChapter = item,
//                    layout = 3,
//                    type = 1
//                )
//                fragment.show(supportFragmentManager, fragment.tag)
//                supportFragmentManager.registerFragmentLifecycleCallbacks(object :
//                    FragmentManager.FragmentLifecycleCallbacks() {
//                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
//                        super.onFragmentViewDestroyed(fm, f)
//                        getKomik(idChapter)
//                        supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
//                    }
//                }, false)
            }

            override fun onItemDeleteClicked(item: Chapter?, posisi: Int) {
                deleteDialog(posisi, item)
            }

        })
    }

    private fun getKomik(id: String) = Coroutines.main {
        adminViewModel.getKomik(id.toInt())
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
            val fragment = FragmentBottomSheet(layout = 4, dataChapter = chapter)
            fragment.show(supportFragmentManager, fragment.tag)
            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    getKomik(idChapter)
                    supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }, false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDialog(posisi: Int, item: Chapter?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Apakah anda ingin menghapus komik detail ${item?.id_detail}?")
        builder.setPositiveButton("YA") { _, _ ->
            Coroutines.main {
                val result = adminViewModel.deleteKomik(item!!.id_detail)
                showToast(this@KomikActivity, result.message)
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
