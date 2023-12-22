package com.itis.android_tasks.ui.adapter.diff_util

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.itis.android_tasks.model.rv.AnimeFavoriteModel

class FavoritesDiffUtil (
    private val oldItemsList: List<AnimeFavoriteModel>,
    private val newItemsList: List<AnimeFavoriteModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition] as? AnimeFavoriteModel
        val newItem = newItemsList[newItemPosition] as? AnimeFavoriteModel
        return oldItem?.animeModel?.name == newItem?.animeModel?.name
                && oldItem?.animeModel?.released == newItem?.animeModel?.released
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return oldItem == newItem
    }
}
