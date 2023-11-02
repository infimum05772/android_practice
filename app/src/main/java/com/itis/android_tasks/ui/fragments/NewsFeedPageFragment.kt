package com.itis.android_tasks.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.itis.android_tasks.R
import com.itis.android_tasks.adapter.NewsFeedAdapter
import com.itis.android_tasks.adapter.decorations.GridHorizontalDecorator
import com.itis.android_tasks.adapter.decorations.ListHorizontalDecorator
import com.itis.android_tasks.adapter.decorations.ListVerticalDecorator
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentNewsFeedPageBinding
import com.itis.android_tasks.model.AddButtonModel
import com.itis.android_tasks.model.DateModel
import com.itis.android_tasks.model.NewsFeedObjectModel
import com.itis.android_tasks.model.NewsModel
import com.itis.android_tasks.ui.holder.NewsItem
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.Constants
import com.itis.android_tasks.utils.NewsGenerator
import com.itis.android_tasks.utils.NewsToAddAmountListener
import com.itis.android_tasks.utils.ParamsKey
import com.itis.android_tasks.utils.getValueInPx
import java.time.LocalDate
import kotlin.random.Random

class NewsFeedPageFragment : Fragment(R.layout.fragment_news_feed_page), NewsToAddAmountListener {
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
        with(binding) {
            context?.let { cont ->
                arguments?.let { args ->
                    val newsCount = args.getInt(ParamsKey.NEWS_COUNT_KEY)
                    val isGridLayoutManager: Boolean

                    if (newsCount <= 12) {
                        rvNews.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        isGridLayoutManager = false
                        rvNews.addItemDecoration(ListHorizontalDecorator(16.getValueInPx(resources.displayMetrics)))

                        ItemTouchHelper(object : ItemTouchHelper.Callback() {
                            override fun getMovementFlags(
                                recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder
                            ): Int = when (viewHolder) {
                                is NewsItem -> makeMovementFlags(0, ItemTouchHelper.LEFT)
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
                                (newsFeed[position] as? NewsModel)?.let { news ->
                                    removeNews(position, news)
                                }
                            }
                        }).attachToRecyclerView(rvNews)

                    } else {
                        rvNews.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int) =
                                    if (newsFeed[position] is NewsModel) Constants.NEWS_SPAN
                                    else Constants.DELIMITER_SPAN
                            }
                        }
                        isGridLayoutManager = true
                        rvNews.addItemDecoration(GridHorizontalDecorator(8.getValueInPx(resources.displayMetrics)))

                    }

                    rvNews.addItemDecoration(ListVerticalDecorator(4.getValueInPx(resources.displayMetrics)))

                    newsAdapter = NewsFeedAdapter(
                        ::onItemClick,
                        ::onLikeClicked,
                        ::onAddButtonClicked,
                        isGridLayoutManager,
                        ::onDelete
                    )

                    rvNews.adapter = newsAdapter

                    if (newsFeed.isEmpty()) {
                        initNewsFeed(newsCount, NewsGenerator.getNews(cont, newsCount))
                    }

                    newsAdapter?.setItems(newsFeed)
                }
            }
        }
    }

    private fun onDelete(position: Int, news: NewsModel) {
        removeNews(position, news)
    }

    private fun onItemClick(news: NewsModel) {
        (requireActivity() as BaseActivity).goToScreen(
            ActionType.REPLACE,
            DetailsPageFragment.newInstance(news),
            DetailsPageFragment.DETAILS_PAGE_FRAGMENT_TAG,
            true
        )
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

    private fun removeNews(position: Int, news: NewsModel) {
        newsFeed.removeAt(position)
        newsAdapter?.setItems(newsFeed)
        this.view?.let {
            Snackbar.make(it, R.string.deleting_news_question, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_btn) {
                    newsFeed.add(position, news)
                    newsAdapter?.setItems(newsFeed)
                }.show()
        }
    }

    override fun onDataReceived(count: Int) {
        context?.let {
            val newsToAdd = NewsGenerator.getNews(it, count)
            for (i in 0 until count) {
                newsFeed.add(Random.nextInt(1, newsFeed.size + 1), newsToAdd[i])
            }
            newsAdapter?.setItems(newsFeed)
        }
    }
}
