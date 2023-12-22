package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentAddDataPageBinding

class AddDataPageFragment : Fragment(R.layout.fragment_add_data_page) {
    private var _binding: FragmentAddDataPageBinding? = null
    private val binding: FragmentAddDataPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDataPageBinding.inflate(inflater, container, false)
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
        const val ADD_DATA_PAGE_FRAGMENT_TAG = "ADD_DATA_PAGE_FRAGMENT_TAG"
    }
}
