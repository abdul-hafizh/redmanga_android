package com.redmanga.apps.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.redmanga.apps.R
import com.redmanga.apps.data.db.entities.Reader
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.utils.convertDate
import kotlinx.android.synthetic.main.item_rv_chapter.view.*

class ChapterAdapter(private val data: List<Chapter>, private val read: List<Int>) :
    RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {
    private var onItemClick: OnItemClick? = null

    fun ItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_chapter, parent, false)
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
        private var tvTanggal: TextView = itemView.tanggal_release
        fun bind(item: Chapter) {
            if(read.contains(item.id_chapter)){
                tvChappter.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey_600))
                tvChappter.text =
                    itemView.resources.getString(R.string.chapter, item.chapter.toString())
                tvJudul.text = item.judul_chapter
                tvJudul.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey_600))
                tvTanggal.text = convertDate(item.tanggal, 2)
                tvTanggal.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey_600))
            }else{
                tvChappter.text =
                    itemView.resources.getString(R.string.chapter, item.chapter.toString())
                tvJudul.text = item.judul_chapter
                tvTanggal.text = convertDate(item.tanggal, 2)
            }

            itemView.setOnClickListener {
                tvChappter.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey_600))
                tvChappter.text =
                    itemView.resources.getString(R.string.chapter, item.chapter.toString())
                tvJudul.text = item.judul_chapter
                tvJudul.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey_600))
                tvTanggal.text = convertDate(item.tanggal, 2)
                tvTanggal.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey_600))
                notifyDataSetChanged()
                onItemClick!!.onItemClicked(item)
            }
        }

    }

    interface OnItemClick {
        fun onItemClicked(item: Chapter?)
    }

}
