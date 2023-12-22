package com.itis.android_tasks.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemAnimeFavoriteBinding
import com.itis.android_tasks.model.rv.AnimeFavoriteModel
import com.itis.android_tasks.model.rv.FavoritesFeedElementModel
import com.itis.android_tasks.ui.adapter.diff_util.FavoritesDiffUtil
import com.itis.android_tasks.ui.holder.AnimeFavoriteItem

class FavoritesAdapter(
    private val onItemClick: (AnimeFavoriteModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favoritesList = mutableListOf<AnimeFavoriteModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        AnimeFavoriteItem(
            onItemClick,
            ItemAnimeFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = favoritesList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AnimeFavoriteItem)?.bindItem(favoritesList[position])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Bundle)?.let {
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
    fun setItems(list: List<AnimeFavoriteModel>) {
        val diff = FavoritesDiffUtil(favoritesList, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        favoritesList.clear()
        favoritesList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(name: String, released: Int) {
        var position: Int? = null
        for (i in favoritesList.indices) {
            if (favoritesList[i].animeModel.name == name && favoritesList[i].animeModel.released == released){
                position = i
            }
        }
        position?.let {
            favoritesList.removeAt(it)
            notifyItemRemoved(it)
        }
    }
}
