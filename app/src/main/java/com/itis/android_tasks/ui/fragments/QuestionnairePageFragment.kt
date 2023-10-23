package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itis.android_tasks.databinding.FragmentQuestionnairePageBinding
import com.itis.android_tasks.R

class QuestionnairePageFragment: Fragment(R.layout.fragment_questionnaire_page) {

    private var _binding: FragmentQuestionnairePageBinding? = null
    private val binding: FragmentQuestionnairePageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionnairePageBinding.inflate(inflater, container, false)
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
        const val QUESTIONNAIRE_PAGE_FRAGMENT_TAG = "QUESTIONNAIRE_PAGE_FRAGMENT_TAG"
    }
}
