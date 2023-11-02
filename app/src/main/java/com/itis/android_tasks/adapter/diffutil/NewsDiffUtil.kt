package com.itis.android_tasks.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.itis.android_tasks.model.NewsFeedObjectModel
import com.itis.android_tasks.model.NewsModel

class NewsDiffUtil(
    private val oldItemsList: List<NewsFeedObjectModel>,
    private val newItemsList: List<NewsFeedObjectModel>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition] as? NewsModel
        val newItem = newItemsList[newItemPosition] as? NewsModel
        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition] as NewsModel
        val newItem = newItemsList[newItemPosition] as NewsModel

        return if (oldItem.isFavorite != newItem.isFavorite) {
            newItem.isFavorite
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}
