package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemAnimeBinding
import com.itis.android_tasks.model.rv.AnimeFeedElementModel

class AnimeItem(
    private val viewBinding: ItemAnimeBinding,
    private val onItemClick: (AnimeFeedElementModel) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: AnimeFeedElementModel? = null

    init {
        viewBinding.root.setOnClickListener {
            item?.let {
                onItemClick.invoke(it)
            }
        }
    }

    fun bindItem(item: AnimeFeedElementModel) {
        this.item = item
        with(viewBinding) {
            tvAnimeName.text = item.animeModel.name
            tvDesc.text = item.animeModel.desc
            tvRating.text = item.rating.toString()
        }
    }
}
