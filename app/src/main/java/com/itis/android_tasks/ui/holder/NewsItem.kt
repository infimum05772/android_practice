package com.itis.android_tasks.ui.holder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.ItemNewsBinding
import com.itis.android_tasks.model.NewsModel

class NewsItem(
    private val viewBinding: ItemNewsBinding,
    private val onNewsClicked: ((NewsModel) -> Unit),
    private val onLikeClicked: ((Int, NewsModel) -> Unit),
    private val isGridLayoutManager: Boolean,
    private val onDelete: ((Int, NewsModel) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: NewsModel? = null
    private var wantToDelete: Boolean = true

    init {
        if (isGridLayoutManager) {
            viewBinding.ivDelete.setOnClickListener {
                this.item?.let { newsModel ->
                    onDelete(adapterPosition, newsModel)
                }
            }
        }
        viewBinding.root.setOnClickListener {
            this.item?.let { newsModel -> onNewsClicked(newsModel) }
        }
        viewBinding.ivStar.setOnClickListener {
            this.item?.let { newsModel ->
                newsModel.isFavorite = !newsModel.isFavorite
                onLikeClicked(adapterPosition, newsModel)
            }
        }
    }

    fun bindItem(item: NewsModel) {
        this.item = item
        with(viewBinding) {
            tvTitle.text = item.title
            tvDesc.text = item.desc
            item.newsImage?.let { res ->
                ivImage.setImageResource(res)
            }
            changeLikeBtnStatus(isChecked = item.isFavorite)
            if (isGridLayoutManager) {
                root.setOnLongClickListener {
                    viewBinding.ivDelete.isVisible = wantToDelete
                    wantToDelete = !wantToDelete
                    true
                }
            }
        }
    }

    fun changeLikeBtnStatus(isChecked: Boolean) {
        val starDrawable = if (isChecked) R.drawable.star_yellow else R.drawable.star_gray
        viewBinding.ivStar.setImageResource(starDrawable)
    }
}
