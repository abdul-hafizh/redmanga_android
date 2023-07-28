package com.redmanga.apps.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.redmanga.apps.R
import com.redmanga.apps.data.preference.PreferenceProvider
import com.redmanga.apps.databinding.ActivityMainBinding
import com.redmanga.apps.databinding.DialogLoginBinding
import com.redmanga.apps.databinding.DialogSearchingBinding
import com.redmanga.apps.ui.admin.PanelActivity
import com.redmanga.apps.ui.home.*
import com.redmanga.apps.utils.Coroutines
import com.redmanga.apps.utils.showEditextError
import com.redmanga.apps.utils.showInputError
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private lateinit var mangaViewModel: MangaViewModel
    private val factory: MangaViewModelFactory by instance()
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var preferenceProvider: PreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        preferenceProvider = PreferenceProvider(this@MainActivity)

        supportActionBar?.title = resources.getString(R.string.app_name)
        mangaViewModel = ViewModelProvider(this, factory).get(MangaViewModel::class.java)

        val viewPager = binding.viewpager
        setupViewPager(viewPager)

        val tabLayout = binding.tabs
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        Coroutines.main {
            mangaViewModel.manga.await()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(NewMangaFragment(), resources.getString(R.string.new_manga))
        adapter.addFragment(MostViewedFragment(), resources.getString(R.string.most_viewed))
        adapter.addFragment(LastestReleaseFragment(), resources.getString(R.string.lastest_release))
        viewPager.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val itemToChange = menu!!.findItem(R.id.admin)
        val itemToHide = menu.findItem(R.id.dashboard)
        if (preferenceProvider.getLogin()!!) {
            itemToChange.icon = ContextCompat.getDrawable(this, R.drawable.ic_exit)
            itemToHide.isVisible = true
        } else {
            itemToChange.icon = ContextCompat.getDrawable(this, R.drawable.ic_person)
            itemToHide.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.admin) {
            if (preferenceProvider.getLogin()!!) {
                logout()
            } else {
                showLoginDialog()
            }
        } else if (item.itemId == R.id.dashboard) {
            startActivity(Intent(this@MainActivity, PanelActivity::class.java))
        } else if (item.itemId == R.id.searching) {
            showSearchDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, resources.getString(R.string.double_press), Toast.LENGTH_SHORT)
            .show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun showLoginDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding: DialogLoginBinding = DialogLoginBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        showEditextError(binding.lyUsername,binding.username,"Username")
        showEditextError(binding.lyPassword,binding.password,"Password")

        binding.btnLogin.setOnClickListener {
            val username: String = binding.username.text.toString()
            val password: String = binding.password.text.toString()

            val cek = arrayOfNulls<Boolean>(2)
            cek[0] = showInputError(binding.lyUsername, username, "Username")
            cek[1] = showInputError(binding.lyPassword, password, "Password")

            if (!cek.contains(false)) {
                Coroutines.main {
                    val hasil = mangaViewModel.loginRequest(username, password)
                    Toast.makeText(this@MainActivity, hasil.message, Toast.LENGTH_SHORT).show()
                    if (hasil.result.token != "null") {
                        dialog.dismiss()
                        preferenceProvider.setLogin(true)
                        preferenceProvider.saveToken(hasil.result.token)
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp
        dialog.show()
    }

    private fun showSearchDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding: DialogSearchingBinding = DialogSearchingBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        showEditextError(binding.lyJudul,binding.judul,"Judul")
        showEditextError(binding.lyPenulis,binding.penulis,"Penulis")

        binding.btnSearching.setOnClickListener {
            val judul: String = binding.judul.text.toString()
            val penulis: String = binding.penulis.text.toString()

            val cek = arrayOfNulls<Boolean>(2)
            cek[0] = showInputError(binding.lyJudul, judul, "Judul")
            cek[1] = showInputError(binding.lyPenulis, penulis, "Penulis")

            if (!cek.contains(false)) {
                Coroutines.main {
                    dialog.dismiss()
                    finish()
                    startActivity(intent)
                }
            }
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp
        dialog.show()
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Apakah anda ingin logout?")
        builder.setPositiveButton("YA") { _, _ ->
            preferenceProvider.setLogin(false)
            finish()
            startActivity(intent)
        }
        builder.setNegativeButton("TIDAK") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
