package com.redmanga.apps.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redmanga.apps.R
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.utils.convertDate
import kotlinx.android.synthetic.main.item_rv_admin_manga.view.*
import kotlinx.android.synthetic.main.item_rv_admin_manga.view.delete

class MangaAdminAdapter(private val data: MutableList<Manga>) :
    RecyclerView.Adapter<MangaAdminAdapter.ViewHolder>() {
    private var onItemClick: OnItemClick? = null

    fun ItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    fun deleteItem(id:Int){
        data.removeAt(id)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_admin_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvJudul: TextView = itemView.judul
        private var tvStatus: TextView = itemView.status
        private var tvTanggal: TextView = itemView.tgl_release
        private var tvPenulis: TextView = itemView.penulis
        private var tvDeskripsi: TextView = itemView.deskripsi
        private var delete: ImageButton = itemView.delete

        fun bind(item: Manga) {
            tvJudul.text = item.judul
            tvStatus.text = item.status
            tvTanggal.text = convertDate(item.tgl_release, 1)
            tvPenulis.text = item.penulis
            tvDeskripsi.text = item.deskripsi

            itemView.setOnClickListener {
                onItemClick!!.onItemClicked(item)
            }

            itemView.setOnLongClickListener {
                onItemClick!!.onLongItemClicked(item)
                false
            }

            delete.setOnClickListener {
                onItemClick!!.onItemDeleteClicked(
                    item,
                    adapterPosition
                )
            }
        }

    }

    interface OnItemClick {
        fun onItemClicked(item: Manga?)
        fun onLongItemClicked(item: Manga?)
        fun onItemDeleteClicked(item: Manga?, posisi: Int)
    }

}
