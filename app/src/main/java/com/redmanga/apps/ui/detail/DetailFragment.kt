package com.redmanga.apps.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.redmanga.apps.databinding.FragmentDetailBinding
import com.redmanga.apps.utils.convertDate
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private lateinit var detailViewModel: DetailViewModel
    private val factory: DetailViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDetailBinding = FragmentDetailBinding.inflate(layoutInflater)
        activity?.let {
            detailViewModel = ViewModelProvider(it, factory).get(DetailViewModel::class.java)
            detailViewModel.data.observe(it, Observer {s->
                s?.let {
                    binding.pengunjung.text = s.pengunjung.toString()
                    binding.penulis.text = s.penulis
                    binding.release.text = convertDate(s.tgl_release,2)
                    binding.genre.text = s.nama_kategori
                    binding.deskripsi.text = s.deskripsi
                    binding.status.text = s.status
                }
            })

            detailViewModel.listChapter.observe(it, Observer {c->
                c?.let{
                    binding.chapter.text = c.size.toString()
                }
            })
        }
        return binding.root
    }

}
