package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentDetailedInfoPageBinding
import com.itis.android_tasks.model.dto.RatingModel
import com.itis.android_tasks.service.impl.AnimeServiceImpl
import com.itis.android_tasks.service.impl.FavoritesServiceImpl
import com.itis.android_tasks.service.impl.RatingServiceImpl
import com.itis.android_tasks.service.impl.UserServiceImpl
import com.itis.android_tasks.session.AppSession
import com.itis.android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailedInfoPageFragment : Fragment(R.layout.fragment_detailed_info_page) {
    private var _binding: FragmentDetailedInfoPageBinding? = null
    private val binding: FragmentDetailedInfoPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedInfoPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        with(binding) {
            arguments?.let { args ->
                val name = args.getString(ParamsKey.ANIME_NAME_BUNDLE_KEY, "")
                val released = args.getInt(ParamsKey.ANIME_RELEASED_BUNDLE_KEY)
                initPage(name, released)

                btnFavorite.setOnClickListener {
                    changeFavorites(name, released)
                }

                btnSaveRating.setOnClickListener {
                    saveRating(name, released)
                }
            }
        }
    }

    private fun saveRating(name: String, released: Int) {
        val rate = binding.rbSetRating.rating.toInt()
        lifecycleScope.launch(Dispatchers.IO) {
            AnimeServiceImpl.getAnimeByNameAndReleased(name, released)?.let { anime ->
                AppSession.getCurrentUser()?.let {
                    RatingServiceImpl.setRating(
                        RatingModel(
                            it.email,
                            anime.name,
                            anime.released,
                            rate
                        )
                    )
                }
                val rating = RatingServiceImpl.getAnimeRatingByAllUsers(anime)
                requireActivity().runOnUiThread {
                    binding.tvRating.text = rating.toString()
                }
            }
        }
    }

    private fun changeFavorites(name: String, released: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            AnimeServiceImpl.getAnimeByNameAndReleased(name, released)?.let { anime ->
                AppSession.getCurrentUser()?.let {
                    val isFavorite = FavoritesServiceImpl.isFavorite(it.email, anime)
                    if (isFavorite) {
                        FavoritesServiceImpl.removeFromFavorites(it.email, anime)
                    } else {
                        FavoritesServiceImpl.addToFavorites(it.email, anime)
                    }
                    requireActivity().runOnUiThread {
                        setIconFavorite(!isFavorite)
                    }
                }
            }
        }
    }

    private fun setIconFavorite(isFavorite: Boolean) {
        val favoriteIcon =
            if (isFavorite)
                R.drawable.icon_remove_from_favorite
            else
                R.drawable.icon_add_to_favorite
        binding.btnFavorite.setImageResource(favoriteIcon)
    }

    private fun initPage(name: String, released: Int) {
        with(binding) {
            lifecycleScope.launch(Dispatchers.IO) {
                AnimeServiceImpl.getAnimeByNameAndReleased(name, released)?.let { anime ->
                    val rating = RatingServiceImpl.getAnimeRatingByAllUsers(anime)
                    AppSession.getCurrentUser()
                        ?.let {
                            val ratingByCurrUser =
                                RatingServiceImpl.getRatingByAnimeAndUser(anime, it.email)
                            val isFavorite = FavoritesServiceImpl.isFavorite(it.email, anime)
                            requireActivity().runOnUiThread {
                                tvAnimeName.text = anime.name
                                tvDesc.text = anime.desc
                                tvReleased.text = anime.released.toString()
                                tvRating.text = rating.toString()
                                rbSetRating.rating = ratingByCurrUser.toFloat()
                                setIconFavorite(isFavorite)
                            }
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val DETAILED_INFO_PAGE_FRAGMENT_TAG = "DETAILED_INFO_PAGE_FRAGMENT_TAG"

        fun newInstance(name: String, released: Int) = DetailedInfoPageFragment().apply {
            arguments = bundleOf(
                ParamsKey.ANIME_NAME_BUNDLE_KEY to name,
                ParamsKey.ANIME_RELEASED_BUNDLE_KEY to released
            )
        }
    }
}
