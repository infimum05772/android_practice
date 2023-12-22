package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemAnimeFavoriteBinding
import com.itis.android_tasks.model.rv.AnimeFavoriteModel
import com.itis.android_tasks.model.rv.AnimeFeedElementModel

class AnimeFavoriteItem(
    private val onItemClick:  (AnimeFavoriteModel) -> Unit,
    private val viewBinding: ItemAnimeFavoriteBinding,
) : RecyclerView.ViewHolder(viewBinding.root)  {

    private var item: AnimeFavoriteModel? = null

    init {
        viewBinding.root.setOnClickListener {
            item?.let {
                onItemClick.invoke(it)
            }
        }
    }
    fun bindItem(item: AnimeFavoriteModel) {
        this.item = item
        with(viewBinding) {
            tvAnimeName.text = item.animeModel.name
            tvDesc.text = item.animeModel.desc
            tvRating.text = item.rating.toString()
        }
    }
}
