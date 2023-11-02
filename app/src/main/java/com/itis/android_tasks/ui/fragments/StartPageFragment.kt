package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.databinding.FragmentStartPageBinding
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.Constants

class StartPageFragment : Fragment(R.layout.fragment_start_page) {

    private var _binding: FragmentStartPageBinding? = null
    private val binding: FragmentStartPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun validateNewsAmount(newsAmount: String): Boolean {
        return newsAmount.isNotEmpty() && Integer.parseInt(newsAmount) in 0..Constants.MAX_NEWS
    }

    private fun initViews() {
        with(binding) {

            etNewsAmount.addTextChangedListener {
                val isNewsAmountCorrect = validateNewsAmount(etNewsAmount.text.toString())
                if (!isNewsAmountCorrect) {
                    tilNewsAmount.error = String.format(getString(R.string.news_to_add_error), 0, Constants.MAX_NEWS)
                } else {
                    tilNewsAmount.error = ""
                }
                btnStart.isEnabled = isNewsAmountCorrect
            }

            btnStart.setOnClickListener {
                (requireActivity() as BaseActivity).goToScreen(
                    ActionType.REPLACE,
                    NewsFeedPageFragment.newInstance(Integer.parseInt(etNewsAmount.text.toString())),
                    NewsFeedPageFragment.NEWS_FEED_PAGE_FRAGMENT_TAG,
                    true
                )
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val START_PAGE_FRAGMENT_TAG = "START_PAGE_FRAGMENT_TAG"
    }
}
