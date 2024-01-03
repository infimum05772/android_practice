package com.itis.android_tasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentThirdPageBinding
import com.itis.android_tasks.utils.ParamsKey

class ThirdPageFragment : Fragment(R.layout.fragment_third_page) {

    private var _binding: FragmentThirdPageBinding? = null
    private val binding: FragmentThirdPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            arguments?.getString(ParamsKey.ENTERED_TEXT_KEY).let { text ->
                if (text.isNullOrEmpty()) {
                    tvEnteredText2.text = context?.getString(R.string.third_page)
                } else {
                    tvEnteredText2.text = text
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val THIRD_PAGE_FRAGMENT_TAG = "THIRD_PAGE_FRAGMENT_TAG"

        fun newInstance(text: String) = ThirdPageFragment().apply {
            arguments = bundleOf(ParamsKey.ENTERED_TEXT_KEY to text)
        }
    }
}
