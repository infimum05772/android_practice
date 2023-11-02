package com.itis.android_tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.itis.android_tasks.R
import com.itis.android_tasks.adapter.diffutil.NewsDiffUtil
import com.itis.android_tasks.databinding.ItemAddButtonBinding
import com.itis.android_tasks.databinding.ItemDateBinding
import com.itis.android_tasks.databinding.ItemNewsBinding
import com.itis.android_tasks.model.AddButtonModel
import com.itis.android_tasks.model.DateModel
import com.itis.android_tasks.model.NewsFeedObjectModel
import com.itis.android_tasks.model.NewsModel
import com.itis.android_tasks.ui.holder.AddButtonItem
import com.itis.android_tasks.ui.holder.DateItem
import com.itis.android_tasks.ui.holder.NewsItem
import java.lang.RuntimeException

class NewsFeedAdapter (
    private val onNewsClicked: ((NewsModel) -> Unit),
    private val onLikeClicked: ((Int, NewsModel) -> Unit),
    private val onAddButtonClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var newsList = mutableListOf<NewsFeedObjectModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType) {
        R.layout.item_add_button -> AddButtonItem (
            ItemAddButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onAddButtonClicked
                )
        R.layout.item_date -> DateItem (
            ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
        R.layout.item_news -> NewsItem (
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onNewsClicked, onLikeClicked
        )
        else -> throw RuntimeException("Unknown item in NewsFeedAdapter")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddButtonItem -> {}
            is DateItem -> (newsList[position] as? DateModel)?.let { holder.bindItem(it) }
            is NewsItem -> (newsList[position] as? NewsModel)?.let { holder.bindItem(item = it) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? NewsItem)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return when (newsList[position]) {
            is AddButtonModel -> R.layout.item_add_button
            is DateModel -> R.layout.item_date
            is NewsModel -> R.layout.item_news
            else -> 0
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun setItems(list: List<NewsFeedObjectModel>) {
        val diff = NewsDiffUtil(oldItemsList = newsList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        newsList.clear()
        newsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, item: NewsModel) {
        this.newsList[position] = item
        notifyItemChanged(position, item.isFavorite)
    }
}
