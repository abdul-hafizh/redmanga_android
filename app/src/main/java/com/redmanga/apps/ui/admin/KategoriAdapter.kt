package com.redmanga.apps.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redmanga.apps.R
import com.redmanga.apps.data.network.response.Kategori
import kotlinx.android.synthetic.main.item_rv_kategori.view.*

class KategoriAdapter(private val data: MutableList<Kategori>) :
    RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    private var onItemClick: OnItemClick? = null

    fun itemClick(onItemClick: OnItemClick?) {
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_kategori, parent, false)
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
        private var name: TextView = itemView.nama
        private var delete: ImageButton = itemView.delete
        fun bind(item: Kategori) {
            name.text = item.nama_kategori
            itemView.setOnClickListener { onItemClick!!.onItemClicked(item) }
            delete.setOnClickListener {
                onItemClick!!.onItemDeleteClicked(
                    item,
                    adapterPosition
                )
            }
        }

    }

    interface OnItemClick {
        fun onItemClicked(item: Kategori?)
        fun onItemDeleteClicked(item: Kategori?, posisi: Int)
    }

}