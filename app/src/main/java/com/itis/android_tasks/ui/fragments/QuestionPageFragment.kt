package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.itis.android_tasks.adapter.AnswersAdapter
import com.itis.android_tasks.databinding.FragmentQuestionPageBinding
import com.itis.android_tasks.model.Answer
import com.itis.android_tasks.model.Question
import com.itis.android_tasks.utils.ParamsKey

class QuestionPageFragment : Fragment() {

    private var _binding: FragmentQuestionPageBinding? = null
    private val binding: FragmentQuestionPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val question = requireArguments().getSerializable(ParamsKey.QUESTION_KEY) as Question
        with(binding) {
            tvQuestionTitle.text = question.title
            tvQuestion.text = question.text
            rvAnswers.adapter = AnswersAdapter(question.answers)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val QUESTION_PAGE_FRAGMENT_TAG = "QUESTION_PAGE_FRAGMENT_TAG"
        fun newInstance(question: Question, position: Int) = QuestionPageFragment().apply {
            arguments = bundleOf(
                ParamsKey.QUESTION_KEY to question,
                ParamsKey.QUESTION_POSITION_KEY to position
            )
        }
    }
}
