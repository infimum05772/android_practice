package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentDetailsPageBinding
import com.itis.android_tasks.model.NewsModel
import com.itis.android_tasks.utils.ParamsKey

class DetailsPageFragment: Fragment(R.layout.fragment_details_page) {

    private var _binding: FragmentDetailsPageBinding? = null
    private val binding: FragmentDetailsPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            (arguments?.getSerializable(ParamsKey.NEWS_MODEL_KEY) as? NewsModel)?.also { news ->
                tvTitle.text = news.title
                tvDescription.text = news.desc
                news.newsImage?.let { ivNewsImage.setImageResource(it) }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val DETAILS_PAGE_FRAGMENT_TAG = "DETAILS_PAGE_FRAGMENT_TAG"
        fun newInstance(newsModel: NewsModel) = DetailsPageFragment().apply {
            arguments = bundleOf(ParamsKey.NEWS_MODEL_KEY to newsModel)
        }
    }
}
