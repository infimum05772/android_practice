package com.itis.android_tasks.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.ItemAnimeBinding
import com.itis.android_tasks.databinding.ItemFavoritesBinding
import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.model.rv.AnimeFavoriteModel
import com.itis.android_tasks.model.rv.AnimeFeedElementModel
import com.itis.android_tasks.model.rv.FavoritesFeedElementModel
import com.itis.android_tasks.model.rv.FeedElementModel
import com.itis.android_tasks.ui.adapter.diff_util.FeedDiffUtil
import com.itis.android_tasks.ui.holder.AnimeFavoriteItem
import com.itis.android_tasks.ui.holder.AnimeItem
import com.itis.android_tasks.ui.holder.FavoritesItem
import com.itis.android_tasks.utils.ParamsKey
import java.lang.RuntimeException

class AnimeFeedAdapter(
    private val onItemFavoriteClick: (AnimeFavoriteModel) -> Unit,
    private val onItemClick: (AnimeFeedElementModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var feedList = mutableListOf<FeedElementModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_favorites -> FavoritesItem(
                onItemFavoriteClick,
                ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            R.layout.item_anime -> AnimeItem(
                ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick
            )

            else -> throw RuntimeException("Unknown item in AnimeFeedAdapter")
        }

    override fun getItemCount(): Int = feedList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavoritesItem -> (feedList[position] as? FavoritesFeedElementModel)?.let {
                holder.bindItem(
                    it
                )
            }

            is AnimeItem -> (feedList[position] as? AnimeFeedElementModel)?.let { holder.bindItem(it) }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Bundle)?.let {
                (holder as? FavoritesItem)?.removeItem(
                    it.getString(
                        ParamsKey.ANIME_NAME_BUNDLE_KEY,
                        ""
                    ), it.getInt(ParamsKey.ANIME_RELEASED_BUNDLE_KEY)
                )
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (feedList[position]) {
            is FavoritesFeedElementModel -> R.layout.item_favorites
            is AnimeFeedElementModel -> R.layout.item_anime
            else -> 0
        }
    }

    fun removeItem(position: Int) {
        if (feedList.isNotEmpty() && feedList[0] is FavoritesFeedElementModel) {
            updateFavorites(feedList[position] as AnimeFeedElementModel)
        }
        feedList.removeAt(position)
        notifyItemRemoved(position)
        if (feedList.isNotEmpty() &&
            feedList[0] is FavoritesFeedElementModel &&
            FavoritesFeedElementModel.getList().size <= 1
        ) {
            feedList.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    fun getModelOnPosition(position: Int): AnimeModel? {
        return (feedList[position] as? AnimeFeedElementModel)?.animeModel
    }

    fun setItems(list: List<FeedElementModel>) {
        val diff = FeedDiffUtil(feedList, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        feedList.clear()
        feedList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun updateFavorites(model: AnimeFeedElementModel) {
        val diff = Bundle()
        diff.putString(ParamsKey.ANIME_NAME_BUNDLE_KEY, model.animeModel.name)
        diff.putInt(ParamsKey.ANIME_RELEASED_BUNDLE_KEY, model.animeModel.released)
        notifyItemChanged(0, diff)
    }
}
