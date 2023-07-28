package com.redmanga.apps.ui.detail

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
import com.redmanga.apps.data.db.entities.Reader
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.databinding.FragmentChapterBinding
import com.redmanga.apps.ui.read.ReadMangaActivity
import com.redmanga.apps.utils.Coroutines
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class ChapterFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var detailViewModel: DetailViewModel
    private val factory: DetailViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChapterBinding = FragmentChapterBinding.inflate(layoutInflater)

        val chapterList: MutableList<Chapter> = mutableListOf()
        val listReader: MutableList<Int> = mutableListOf()
        val adapter = ChapterAdapter(chapterList, listReader)
        binding.rvChapter.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(
            binding.rvChapter.context,
            LinearLayoutManager.VERTICAL
        )
        binding.rvChapter.addItemDecoration(dividerItemDecoration)
        binding.rvChapter.setHasFixedSize(true)
        binding.rvChapter.adapter = adapter

        var id = 0
        activity?.let {
            detailViewModel = ViewModelProvider(it, factory).get(DetailViewModel::class.java)
            detailViewModel.read.observe(it, Observer { r ->
                r?.let {
                    listReader.addAll(r)
                }
            })

            detailViewModel.listChapter.observe(it, Observer { c ->
                c?.let {
                    chapterList.clear()
                    chapterList.addAll(c)
                    adapter.notifyDataSetChanged()
                }
            })

            detailViewModel.data.observe(it, Observer { b ->
                b?.let {
                    id = b.id_manga
                }
            })

        }

        adapter.ItemClick(object : ChapterAdapter.OnItemClick {
            override fun onItemClicked(item: Chapter?) {
                detailViewModel.saveRead(Reader(item!!.id_chapter, id, true))
                val intent = Intent(activity!!, ReadMangaActivity::class.java)
                intent.putExtra(ReadMangaActivity.EXTRA_DATA, item)
                intent.putParcelableArrayListExtra(
                    ReadMangaActivity.EXTRA_LIST,
                    ArrayList(chapterList)
                )
                startActivity(intent)
            }
        })
        return binding.root
    }

}
