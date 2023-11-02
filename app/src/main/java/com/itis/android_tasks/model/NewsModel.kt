package com.itis.android_tasks.model

import androidx.annotation.DrawableRes

data class NewsModel(
    val id: Int,
    val title: String,
    val desc: String,
    @DrawableRes val newsImage: Int? = null,
    var isFavorite: Boolean
) : NewsFeedObjectModel {
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + (newsImage ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsModel

        if (title != other.title) return false
        if (desc != other.desc) return false
        if (newsImage != other.newsImage) return false

        return true
    }
}
