package com.itis.android_tasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentSecondPageBinding
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.ParamsKey

class SecondPageFragment : Fragment(R.layout.fragment_second_page) {

    private var _binding: FragmentSecondPageBinding? = null
    private val binding: FragmentSecondPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            val text = arguments?.getString(ParamsKey.ENTERED_TEXT_KEY).also { text ->
                if (text.isNullOrEmpty()) {
                    tvEnteredText1.text = context?.getString(R.string.second_page)
                } else {
                    tvEnteredText1.text = text
                }
            }
            btnToFirstFragment.setOnClickListener {
                (requireActivity() as BaseActivity).supportFragmentManager.popBackStack();
            }
            btnToThirdFragment.setOnClickListener {
                (requireActivity() as BaseActivity).supportFragmentManager.popBackStack();
                (requireActivity() as BaseActivity).goToScreen(
                    ActionType.REPLACE,
                    ThirdPageFragment.newInstance(text!!),
                    ThirdPageFragment.THIRD_PAGE_FRAGMENT_TAG,
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
        const val SECOND_PAGE_FRAGMENT_TAG = "SECOND_PAGE_FRAGMENT_TAG"

        fun newInstance(text: String) = SecondPageFragment().apply {
            arguments = bundleOf(ParamsKey.ENTERED_TEXT_KEY to text)
        }
    }
}
