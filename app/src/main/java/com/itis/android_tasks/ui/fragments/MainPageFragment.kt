package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentMainPageBinding
import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.model.rv.AnimeFavoriteModel
import com.itis.android_tasks.model.rv.AnimeFeedElementModel
import com.itis.android_tasks.model.rv.FavoritesFeedElementModel
import com.itis.android_tasks.model.rv.FeedElementModel
import com.itis.android_tasks.service.impl.AnimeServiceImpl
import com.itis.android_tasks.service.impl.RatingServiceImpl
import com.itis.android_tasks.service.impl.UserServiceImpl
import com.itis.android_tasks.session.AppSession
import com.itis.android_tasks.ui.adapter.AnimeFeedAdapter
import com.itis.android_tasks.ui.adapter.decorations.ListHorizontalDecorator
import com.itis.android_tasks.ui.adapter.decorations.ListVerticalDecorator
import com.itis.android_tasks.ui.holder.AnimeItem
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.getValueInPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.FieldPosition

class MainPageFragment : Fragment(R.layout.fragment_main_page) {

    private var _binding: FragmentMainPageBinding? = null

    private var feedAdapter: AnimeFeedAdapter? = null
    private val binding: FragmentMainPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        feedAdapter = AnimeFeedAdapter(::onItemClick, ::onItemClick)
        with(binding.rvFeed) {
            if (itemDecorationCount < 2) {
                addItemDecoration(ListVerticalDecorator(4.getValueInPx(resources.displayMetrics)))
                addItemDecoration(ListHorizontalDecorator(8.getValueInPx(resources.displayMetrics)))
            }

            ItemTouchHelper(object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int = when (viewHolder) {
                    is AnimeItem -> makeMovementFlags(0, ItemTouchHelper.LEFT)
                    else -> 0
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition
                    removeAnime(position)
                }
            }).attachToRecyclerView(this)

            adapter = feedAdapter

        }

        initFeed()
    }

    private fun removeAnime(position: Int) {
        val animeToDelete = feedAdapter?.getModelOnPosition(position)
        feedAdapter?.removeItem(position)
        lifecycleScope.launch(Dispatchers.IO) {
            if (animeToDelete != null) {
                AnimeServiceImpl.delete(animeToDelete)
            }
        }
        binding.tvNoAnimeFound.isVisible = feedAdapter?.itemCount == 0
    }

    private fun initFeed() {
        lifecycleScope.launch(Dispatchers.IO) {
            AnimeServiceImpl.getAllAnime().let { animeModels ->
                requireActivity().runOnUiThread {
                    binding.tvNoAnimeFound.isVisible = animeModels.isNullOrEmpty()
                }
                if (!animeModels.isNullOrEmpty()) {
                    val feed = mutableListOf<FeedElementModel>()
                    val favoritesFeed = mutableListOf<AnimeFavoriteModel>()

                    val favorites = AppSession.getCurrentUser()?.let {
                        AnimeServiceImpl.getUserFavoriteAnime(it.email)
                    }

                    for (animeModel in animeModels) {
                        val rate = RatingServiceImpl.getAnimeRatingByAllUsers(animeModel)
                        feed.add(
                            AnimeFeedElementModel(
                                animeModel,
                                rate
                            )
                        )
                        favorites?.let {
                            if (animeModel in favorites) {
                                favoritesFeed.add(
                                    AnimeFavoriteModel(
                                        animeModel,
                                        rate
                                    )
                                )
                            }
                        }
                    }

                    if (!favorites.isNullOrEmpty()) {
                        FavoritesFeedElementModel.setList(favoritesFeed)
                        feed.add(
                            0, FavoritesFeedElementModel
                        )
                    }

                    requireActivity().runOnUiThread {
                        feedAdapter?.setItems(feed)
                    }
                }
            }
        }
    }

    private fun toDetailsFragment(name: String, released: Int) {
        (requireActivity() as? BaseActivity)?.goToScreen(
            ActionType.REPLACE,
            DetailedInfoPageFragment.newInstance(name, released),
            DetailedInfoPageFragment.DETAILED_INFO_PAGE_FRAGMENT_TAG,
            true
        )
    }

    private fun onItemClick(item: AnimeFavoriteModel) {
        toDetailsFragment(item.animeModel.name, item.animeModel.released)
    }

    private fun onItemClick(item: AnimeFeedElementModel) {
        toDetailsFragment(item.animeModel.name, item.animeModel.released)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val MAIN_PAGE_FRAGMENT_TAG = "MAIN_PAGE_FRAGMENT_TAG"
    }
}
