package com.redmanga.apps.ui.admin

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmanga.apps.R
import com.redmanga.apps.data.network.response.Kategori
import com.redmanga.apps.databinding.ActivityKategoriBinding
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class KategoriActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private lateinit var adminViewModel: AdminViewModel
    private val factory: AdminViewModelFactory by instance()
    lateinit var adapter: KategoriAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityKategoriBinding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "List Kategori"

        adminViewModel = ViewModelProvider(this, factory).get(AdminViewModel::class.java)

        getKategori()

        val kategoriList: MutableList<Kategori> = mutableListOf()
        adapter = KategoriAdapter(kategoriList)
        binding.rvKateogri.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvKateogri.context,
            LinearLayoutManager.VERTICAL
        )
        binding.rvKateogri.addItemDecoration(dividerItemDecoration)
        binding.rvKateogri.setHasFixedSize(true)
        binding.rvKateogri.adapter = adapter

        adminViewModel.kategori.observe(this, Observer {
            it?.let {
                kategoriList.clear()
                kategoriList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        adapter.itemClick(object : KategoriAdapter.OnItemClick {
            override fun onItemClicked(item: Kategori?) {
                val fragment = FragmentBottomSheet(kategori = item, layout = 1, type = 1)
                fragment.show(supportFragmentManager, fragment.tag)
                supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                    FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentViewDestroyed(fm, f)
                        getKategori()
                        supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                    }
                }, false)
            }

            override fun onItemDeleteClicked(item: Kategori?, posisi: Int) {
                deleteDialog(posisi, item)
            }

        })
    }

    private fun getKategori() = Coroutines.main {
        adminViewModel.getKategori()
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
            val fragment = FragmentBottomSheet(layout = 1)
            fragment.show(supportFragmentManager, fragment.tag)
            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    getKategori()
                    supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }, false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDialog(posisi: Int, item: Kategori?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Apakah anda ingin menghapus kategori ${item?.nama_kategori}?")
        builder.setPositiveButton("YA") { _, _ ->
            Coroutines.main {
                val result = adminViewModel.deleteKategori(item!!.id_kategori)
                showToast(this@KategoriActivity, result.message)
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
