package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemFavoritesBinding
import com.itis.android_tasks.model.rv.AnimeFavoriteModel
import com.itis.android_tasks.model.rv.AnimeFeedElementModel
import com.itis.android_tasks.model.rv.FavoritesFeedElementModel
import com.itis.android_tasks.ui.adapter.FavoritesAdapter
import com.itis.android_tasks.ui.adapter.decorations.ListHorizontalDecorator
import com.itis.android_tasks.ui.adapter.decorations.ListVerticalDecorator
import com.itis.android_tasks.utils.getValueInPx

class FavoritesItem(
    private val onItemClick: (AnimeFavoriteModel) -> Unit,
    private val viewBinding: ItemFavoritesBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: FavoritesFeedElementModel? = null
    private var adapterFavorites: FavoritesAdapter? = null

    fun bindItem(item: FavoritesFeedElementModel) {
        this.item = item
        adapterFavorites = FavoritesAdapter(onItemClick)
        with(viewBinding.rvFavorites) {
            if (itemDecorationCount < 1){
                addItemDecoration(ListHorizontalDecorator(4.getValueInPx(resources.displayMetrics)))
            }
            adapter = adapterFavorites
        }
        adapterFavorites?.setItems(
            item.getList()
        )
    }

    fun removeItem(name: String, released: Int) {
        adapterFavorites?.removeItem(name, released)
    }
}
