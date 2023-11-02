package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.ItemNewsBinding
import com.itis.android_tasks.model.NewsModel

class NewsItem(
    private val viewBinding: ItemNewsBinding,
    private val onNewsClicked: ((NewsModel) -> Unit),
    private val onLikeClicked: ((Int, NewsModel) -> Unit)
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: NewsModel? = null
    init {
        viewBinding.root.setOnClickListener {
            this.item?.let(onNewsClicked)
        }
        viewBinding.ivStar.setOnClickListener {
            this.item?.let {
                it.isFavorite = !it.isFavorite
                onLikeClicked(adapterPosition, it)
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
        }
    }

    fun changeLikeBtnStatus(isChecked: Boolean) {
        val starDrawable = if (isChecked) R.drawable.star_yellow else R.drawable.star_gray
        viewBinding.ivStar.setImageResource(starDrawable)
    }
}
