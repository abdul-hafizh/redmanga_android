package com.redmanga.apps.ui.home

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.redmanga.apps.R
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.utils.convertDate
import com.redmanga.apps.utils.displayImage
import kotlinx.android.synthetic.main.item_rv_manga.view.*


class MangaAdapter(private val context: Context) :
    PagedListAdapter<Manga, MangaAdapter.MangaViewHolder>(MangaDiffCallback()) {

    private var listener: OnItemListener? = null

    fun setOnItemListener(listener: OnItemListener?) {
        this.listener = listener
    }

    override fun onBindViewHolder(holderManga: MangaViewHolder, position: Int) {
        val manga = getItem(position)
        if (manga == null) {
            holderManga.clear()
        } else {
            holderManga.bind(manga)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        return MangaViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rv_manga, parent, false)
        )
    }

    inner class MangaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var tvName: TextView = view.judul
        private var tvViewer: TextView = view.viewer
        private var chipGenre: TextView = view.genre
        private var tvStatus: Chip = view.status
        private var tvRelease: TextView = view.date_release
        private var image: ImageView = view.image
        private var item = view

        fun bind(manga: Manga) {
            tvName.text = manga.judul
            chipGenre.text = manga.nama_kategori
            tvRelease.text = convertDate(manga.tgl_release, 1)
            if(manga.status == "Complete"){
                tvStatus.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(item.context, R.color.green_600))
            }else{
                tvStatus.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(item.context, R.color.orange_600))
            }
            tvStatus.text = manga.status
            tvViewer.text = manga.pengunjung.toString()
            displayImage(item.context, image, manga.cover)

            item.setOnClickListener { listener!!.onItemClick(manga) }
        }

        fun clear() {
            tvName.text = null
            tvViewer.text = null
            tvStatus.text = null
            chipGenre.visibility = View.GONE
            tvRelease.text = null
            displayImage(item.context, image)
        }
    }

    interface OnItemListener {
        fun onItemClick(item: Manga)
    }
}