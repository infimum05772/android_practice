package com.itis.android_tasks.ui

import android.content.res.Configuration
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.mainFragmentRightContainer.visibility = View.VISIBLE
        } else {
            binding.mainFragmentRightContainer.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        if (childFragmentManager.findFragmentByTag(FirstPageFragment.FIRST_PAGE_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                .add(binding.mainFragmentLeftContainer.id, FirstPageFragment(), FirstPageFragment.FIRST_PAGE_FRAGMENT_TAG)
                .add(binding.mainFragmentRightContainer.id, FourthPageFragment(), FourthPageFragment.FOURTH_PAGE_FRAGMENT_TAG)
                .commit()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
    }
}
