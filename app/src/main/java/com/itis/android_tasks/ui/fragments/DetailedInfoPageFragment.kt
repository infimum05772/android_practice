package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentDetailedInfoPageBinding

class DetailedInfoPageFragment: Fragment(R.layout.fragment_detailed_info_page) {
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

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val DETAILED_INFO_PAGE_FRAGMENT_TAG = "DETAILED_INFO_PAGE_FRAGMENT_TAG"
    }
}
