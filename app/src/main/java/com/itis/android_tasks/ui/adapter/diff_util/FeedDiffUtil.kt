package com.itis.android_tasks.ui.adapter.diff_util

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.itis.android_tasks.model.rv.AnimeFeedElementModel
import com.itis.android_tasks.model.rv.FeedElementModel

class FeedDiffUtil(
    private val oldItemsList: List<FeedElementModel>,
    private val newItemsList: List<FeedElementModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition] as? AnimeFeedElementModel
        val newItem = newItemsList[newItemPosition] as? AnimeFeedElementModel
        return oldItem?.animeModel?.name == newItem?.animeModel?.name
                && oldItem?.animeModel?.released == newItem?.animeModel?.released
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return oldItem == newItem
    }
}
