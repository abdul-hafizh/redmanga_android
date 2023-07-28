package com.redmanga.apps.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redmanga.apps.R
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.utils.convertDate
import kotlinx.android.synthetic.main.item_rv_admin_chapter.view.*
import kotlinx.android.synthetic.main.item_rv_admin_chapter.view.delete

class ChapterAdminAdapter(private val data: MutableList<Chapter>) :
    RecyclerView.Adapter<ChapterAdminAdapter.ViewHolder>() {
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
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_admin_chapter, parent, false)
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
        private var tvChappter: TextView = itemView.chapter
        private var tvJudul: TextView = itemView.judul_chapter
        private var tvTanggal: TextView = itemView.tanggal
        private var delete: ImageButton = itemView.delete

        fun bind(item: Chapter) {
            tvChappter.text =
                itemView.resources.getString(R.string.chapter, item.chapter.toString())
            tvJudul.text = item.judul_chapter
            tvTanggal.text = convertDate(item.tanggal, 2)

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
        fun onItemClicked(item: Chapter?)
        fun onLongItemClicked(item: Chapter?)
        fun onItemDeleteClicked(item: Chapter?, posisi: Int)
    }

}
