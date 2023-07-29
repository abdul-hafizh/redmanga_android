package com.redmanga.apps.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.databinding.FragmentNewMangaBinding
import com.redmanga.apps.ui.MainActivity
import com.redmanga.apps.ui.detail.DetailMangaActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class NewMangaFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var mangaViewModel: MangaViewModel
    private val factory: MangaViewModelFactory by instance()
    private val adapter by lazy { MangaAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNewMangaBinding = FragmentNewMangaBinding.inflate(layoutInflater)
        mangaViewModel = ViewModelProvider(this, factory).get(MangaViewModel::class.java)

        binding.rvManga.layoutManager = LinearLayoutManager(context)
        binding.rvManga.setHasFixedSize(true)
        binding.rvManga.adapter = adapter
        subscribeUi()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onMessageEvent(event: MainActivity.MangaSearchEvent) {
        adapter.submitList(event.manga)
    }

    private fun subscribeUi() {
        mangaViewModel.pagedListLiveDataNewManga.observe(viewLifecycleOwner, Observer { names ->
            if (names != null) adapter.submitList(names)
        })

        adapter.setOnItemListener(object : MangaAdapter.OnItemListener {
            override fun onItemClick(item: Manga) {
                val intent = Intent(activity!!, DetailMangaActivity::class.java)
                intent.putExtra(DetailMangaActivity.EXTRA_DATA, item)
                startActivity(intent)
            }
        })
    }
}
