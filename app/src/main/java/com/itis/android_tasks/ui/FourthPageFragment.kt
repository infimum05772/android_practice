package com.itis.android_tasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentFourthPageBinding
import com.itis.android_tasks.utils.DataListener

class FourthPageFragment : Fragment(R.layout.fragment_fourth_page), DataListener {

    private var _binding: FragmentFourthPageBinding? = null
    private val binding: FragmentFourthPageBinding
        get() = _binding!!

    private lateinit var textViews: ArrayList<TextView>

    private var index: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFourthPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            textViews = ArrayList()
            textViews.add(tvEnteredTextLine1)
            textViews.add(tvEnteredTextLine2)
            textViews.add(tvEnteredTextLine3)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val FOURTH_PAGE_FRAGMENT_TAG = "FOURTH_PAGE_FRAGMENT_TAG"
    }

    override fun onDataReceived(data: String) {
        textViews[index].text = data
        index = (index + 1) % 3
    }
}
