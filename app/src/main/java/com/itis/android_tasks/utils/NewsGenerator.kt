package com.itis.android_tasks.utils

import android.content.Context
import com.itis.android_tasks.R
import com.itis.android_tasks.model.NewsModel
import kotlin.random.Random

object NewsGenerator {

    private val images: List<Int> = listOf(
        R.drawable.news1,
        R.drawable.news2,
        R.drawable.news3,
        R.drawable.news4,
        R.drawable.news5,
        R.drawable.news6,
        R.drawable.news7,
        R.drawable.news8
    )

    fun getNews(ctx: Context, newsCount: Int): MutableList<NewsModel> {
        val titles = ctx.resources.getStringArray(R.array.news_titles)
        val descriptions = ctx.resources.getStringArray(R.array.news_desc)

        val resultList = mutableListOf<NewsModel>()
        var index = 0
        repeat(newsCount) {
            val newIndexTitle = Random.nextInt(0, titles.size)
            val newIndexDesc = Random.nextInt(0, descriptions.size)
            val newIndexImage = Random.nextInt(0, images.size)
            resultList.add(
                NewsModel(
                    index++,
                    titles[newIndexTitle],
                    descriptions[newIndexDesc],
                    images[newIndexImage],
                    isFavorite = false,
                    wantToDelete = false
                )
            )
        }
        return resultList
    }
}
