package com.itis.android_tasks.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.itis.android_tasks.R
import com.itis.android_tasks.adapter.NewsFeedAdapter
import com.itis.android_tasks.adapter.decorations.GridHorizontalDecorator
import com.itis.android_tasks.adapter.decorations.ListHorizontalDecorator
import com.itis.android_tasks.adapter.decorations.ListVerticalDecorator
import com.itis.android_tasks.databinding.FragmentNewsFeedPageBinding
import com.itis.android_tasks.model.AddButtonModel
import com.itis.android_tasks.model.DateModel
import com.itis.android_tasks.model.NewsFeedObjectModel
import com.itis.android_tasks.model.NewsModel
import com.itis.android_tasks.utils.NewsGenerator
import com.itis.android_tasks.utils.ParamsKey
import com.itis.android_tasks.utils.getValueInPx
import java.time.LocalDate

class NewsFeedPageFragment: Fragment(R.layout.fragment_news_feed_page) {
    private var _binding: FragmentNewsFeedPageBinding? = null
    private val binding: FragmentNewsFeedPageBinding
        get() = _binding!!

    private var newsFeed: MutableList<NewsFeedObjectModel> = mutableListOf()
    private var newsAdapter: NewsFeedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsFeedPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        newsAdapter = NewsFeedAdapter({}, ::onLikeClicked, ::onAddButtonClicked)
        with(binding) {
            context?.let {cont ->
                arguments?.let { args ->
                    val newsCount = args.getInt(ParamsKey.NEWS_COUNT_KEY)
                    val marginValue = 16.getValueInPx(resources.displayMetrics)

                    if (newsCount <= 12) {
                        rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        rvNews.addItemDecoration(ListHorizontalDecorator(marginValue.getValueInPx(resources.displayMetrics)))
                    } else {
                        rvNews.layoutManager = StaggeredGridLayoutManager(marginValue / 8, LinearLayoutManager.VERTICAL)
                        rvNews.addItemDecoration(GridHorizontalDecorator(marginValue / 2.getValueInPx(resources.displayMetrics)))
                    }

                    rvNews.addItemDecoration(ListVerticalDecorator(marginValue / 4.getValueInPx(resources.displayMetrics)))

                    rvNews.adapter = newsAdapter

                    initNewsFeed(newsCount, NewsGenerator.getNews(cont, newsCount))
                    newsAdapter?.setItems(newsFeed)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNewsFeed(newsCount: Int, news: List<NewsModel>) {
        newsFeed.clear()
        newsFeed.add(AddButtonModel)
        if (newsCount <= 12) {
            for (i in 0 until newsCount) {
                newsFeed.add(news[i])
            }
        } else {
            for (i in 0 until newsCount) {
                if (i % 8 == 0) {
                    newsFeed.add(DateModel(LocalDate.now().minusDays((i / 8).toLong())))
                }
                newsFeed.add(news[i])
            }
        }
    }

    private fun onLikeClicked(position: Int, newsModel: NewsModel) {
        newsAdapter?.updateItem(position, newsModel)
    }

    private fun onAddButtonClicked() {
        AddNewsFragment().show(childFragmentManager, AddNewsFragment.ADD_NEWS_FRAGMENT_TAG)
    }

    override fun onDestroyView() {
        _binding = null
        newsAdapter = null
        super.onDestroyView()
    }

    companion object {
        const val NEWS_FEED_PAGE_FRAGMENT_TAG = "NEWS_FEED_PAGE_FRAGMENT_TAG"
        fun newInstance(newsCount: Int) = NewsFeedPageFragment().apply {
            arguments = bundleOf(ParamsKey.NEWS_COUNT_KEY to newsCount)
        }
    }
}
