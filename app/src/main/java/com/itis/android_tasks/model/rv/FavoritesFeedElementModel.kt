package com.itis.android_tasks.model.rv

object FavoritesFeedElementModel : FeedElementModel {

    private var favoritesList: MutableList<AnimeFavoriteModel> = mutableListOf()


    fun setList(list: MutableList<AnimeFavoriteModel>) {
        favoritesList = list
    }

    fun getList() : MutableList<AnimeFavoriteModel> {
        return favoritesList
    }

    fun removeItem(name: String, released: Int) {
        for (i in favoritesList.indices) {
            if (favoritesList[i].animeModel.name == name && favoritesList[i].animeModel.released == released){
                favoritesList.removeAt(i)
            }
        }
    }
}
