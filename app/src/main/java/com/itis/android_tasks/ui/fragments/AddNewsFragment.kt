package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentAddNewsBinding
import com.itis.android_tasks.utils.Constants

class AddNewsFragment : BottomSheetDialogFragment(R.layout.fragment_add_news) {

    private var _binding: FragmentAddNewsBinding? = null
    private val binding: FragmentAddNewsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun validateNewsAmount(newsAmount: String): Boolean {
        return newsAmount.isNotEmpty() && Integer.parseInt(newsAmount) in 1..Constants.MAX_NEWS_TO_ADD
    }

    private fun initViews() {
        with(binding) {

            etNewsToAddAmount.addTextChangedListener {
                val isNewsAmountCorrect = validateNewsAmount(etNewsToAddAmount.text.toString())
                if (!isNewsAmountCorrect) {
                    tilNewsToAddAmount.error = String.format(
                        getString(R.string.news_to_add_error),
                        1,
                        Constants.MAX_NEWS_TO_ADD
                    )
                } else {
                    tilNewsToAddAmount.error = ""
                }
                btnAdd.isEnabled = isNewsAmountCorrect
            }

            btnAdd.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ADD_NEWS_FRAGMENT_TAG = "ADD_NEWS_FRAGMENT_TAG"
    }
}
