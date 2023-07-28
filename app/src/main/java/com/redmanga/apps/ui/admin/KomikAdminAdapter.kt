package com.redmanga.apps.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redmanga.apps.R
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.utils.convertDate
import com.redmanga.apps.utils.displayImage
import kotlinx.android.synthetic.main.item_rv_admin_komik.view.*

class KomikAdminAdapter(private val data: MutableList<Chapter>) :
    RecyclerView.Adapter<KomikAdminAdapter.ViewHolder>() {
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
                .inflate(R.layout.item_rv_admin_komik, parent, false)
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
        private var gambar: ImageView = itemView.image
        private var delete: ImageButton = itemView.delete

        fun bind(item: Chapter) {
            displayImage(itemView.context,gambar,item.gambar,1)

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