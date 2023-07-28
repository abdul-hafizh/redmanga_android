package com.redmanga.apps.ui.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.redmanga.apps.R
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.utils.displayImage
import kotlinx.android.synthetic.main.item_rv_read_manga.view.*

class ReadMangaAdapter(private val data: List<Chapter>) :
    RecyclerView.Adapter<ReadMangaAdapter.ViewHolder>() {
    private var onItemClick: OnItemClick? = null

    fun ItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_read_manga, parent, false)
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
        private var image: ImageView = itemView.image
        fun bind(item: Chapter) {
            displayImage(itemView.context,image,item.gambar,1)
            itemView.setOnClickListener { onItemClick!!.onItemClicked(item) }
        }

    }

    interface OnItemClick {
        fun onItemClicked(item: Chapter?)
    }

}