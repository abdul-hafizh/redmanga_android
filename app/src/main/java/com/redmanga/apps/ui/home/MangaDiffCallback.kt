package com.redmanga.apps.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.redmanga.apps.data.db.entities.Manga

class MangaDiffCallback : DiffUtil.ItemCallback<Manga>() {

    override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
        return oldItem.id_manga == newItem.id_manga
    }

    override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
        return oldItem == newItem
    }
}