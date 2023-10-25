package com.itis.android_tasks.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.itis.android_tasks.databinding.FragmentQuestionnairePageBinding
import com.itis.android_tasks.R
import com.itis.android_tasks.adapter.QuestionnaireAdapter
import com.itis.android_tasks.model.Question
import com.itis.android_tasks.utils.DataListener
import com.itis.android_tasks.utils.ParamsKey
import com.itis.android_tasks.utils.QuestionGenerator

class QuestionnairePageFragment : Fragment(R.layout.fragment_questionnaire_page), DataListener {

    private var _binding: FragmentQuestionnairePageBinding? = null
    private val binding: FragmentQuestionnairePageBinding
        get() = _binding!!

    private var questions: List<Question>? = null

    private var answered: MutableList<Boolean> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnairePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(binding) {
            context?.let { cont ->
                arguments?.let { args ->
                    val questionCount = args.getInt(ParamsKey.QUESTION_COUNT_KEY)
                    repeat(questionCount) {
                        answered.add(false)
                    }
                    questions = QuestionGenerator.getQuestions(
                        cont,
                        questionCount
                    )
                    questions?.let {
                        vpQuestions.apply {
                            adapter = QuestionnaireAdapter(it, parentFragmentManager, lifecycle)
                            registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {

                                override fun onPageSelected(position: Int) {
                                    if (position == questionCount || btnFinish.isEnabled) {
                                        btnFinish.visibility = View.VISIBLE
                                    } else {
                                        btnFinish.visibility = View.GONE
                                    }
                                }

                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                    if (position == questionCount + 1 && positionOffset == 0.0f) {
                                        setCurrentItem(1, false)
                                    } else if (position == 0 && positionOffset == 0.0f) {
                                        setCurrentItem(questionCount, false)
                                    }
                                }
                            })
                            setCurrentItem(1, false)
                        }
                    }
                }
            }
            btnFinish.setOnClickListener {
                Toast.makeText(context, "questionnaire successfully completed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val QUESTIONNAIRE_PAGE_FRAGMENT_TAG = "QUESTIONNAIRE_PAGE_FRAGMENT_TAG"
        fun newInstance(questionCount: Int) = QuestionnairePageFragment().apply {
            arguments = bundleOf(ParamsKey.QUESTION_COUNT_KEY to questionCount)
        }
    }

    override fun onDataReceived(position: Int) {
        answered[position] = true
        if (answered.all { question -> question }) {
            binding.btnFinish.isEnabled = true
        }
    }
}
