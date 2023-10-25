package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.databinding.FragmentStartPageBinding
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.Constants
import com.itis.android_tasks.utils.TextWatcherActionType

class StartPageFragment : Fragment(R.layout.fragment_start_page) {

    private var _binding: FragmentStartPageBinding? = null
    private val binding: FragmentStartPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun validatePhone(phone: String): Boolean {
        return phone.length == 18 && phone.matches(Regex("^\\+7 \\(9\\d{2}\\)-\\d{3}-\\d{2}-\\d{2}$"))
    }

    private fun validateQuestionsAmount(questionsAmount: String): Boolean {
        return !(questionsAmount.isEmpty() || questionsAmount[0] == '0' || Integer.parseInt(
            questionsAmount
        ) > Constants.MAX_QUESTIONS)
    }

    private fun validateAll(phone: String, questionsAmount: String): Boolean {
        return validatePhone(phone) && validateQuestionsAmount(questionsAmount)
    }

    private fun initViews() {
        with(binding) {

            etPhone.addTextChangedListener(object : TextWatcher {

                private fun doSetTextTransaction(
                    input: CharSequence,
                    action: TextWatcherActionType,
                    selection: Int,
                    count: Int
                ) {
                    etPhone.removeTextChangedListener(this)
                    etPhone.setText(input.let {
                        when (action) {
                            TextWatcherActionType.ADD -> {
                                addNumbersAutocomplete(input, selection, count)
                            }

                            TextWatcherActionType.REMOVE -> {
                                removeNumbersAutocomplete(input)
                            }
                        }
                    })
                    etPhone.setSelection(etPhone.text?.length ?: 0)
                    etPhone.addTextChangedListener(this)
                }

                private fun addNumbersAutocomplete(
                    input: CharSequence,
                    selection: Int,
                    count: Int
                ): CharSequence {
                    var inputModified = input
                    if (selection != input.length && count <= 1) {
                        input.subSequence(selection, selection + count).let {
                            inputModified = input.removeRange(selection, selection + count)
                                .toString() + it
                        }
                    }
                    if (input.length == 7) {
                        return "${inputModified})-"
                    }
                    if (input.length == 12 || input.length == 15) {
                        return "${inputModified}-"
                    }
                    return inputModified
                }

                private fun removeNumbersAutocomplete(
                    input: CharSequence
                ): CharSequence {
                    if (input.length <= 5) {
                        return "+7 (9"
                    }
                    if (input.length == 9) {
                        return input.subSequence(0, input.length - 3)
                    }
                    if (input.length == 13 || input.length == 16) {
                        return input.subSequence(0, input.length - 2)
                    }
                    return input.subSequence(0, input.length - 1)
                }

                override fun beforeTextChanged(
                    input: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    input?.let {
                        if (after == 0) {
                            doSetTextTransaction(input, TextWatcherActionType.REMOVE, start, count)
                        }
                    }
                }

                override fun onTextChanged(
                    input: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    input?.let {
                        if (before == 0) {
                            doSetTextTransaction(input, TextWatcherActionType.ADD, start, count)
                        }
                    }
                }

                override fun afterTextChanged(input: Editable?) {
                    input?.let {
                        if (validatePhone(input.toString())) {
                            tilPhone.error = ""
                        }
                        btnStart.isEnabled =
                            validateAll(input.toString(), etQuestionsAmount.text.toString())
                    }
                }
            })

            etPhone.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    if (!validatePhone(etPhone.text.toString())) {
                        tilPhone.error = "phone number must contain 10 digits and match mask\n+7 (9##)-###-##-##"
                    }
                } else if (etPhone.text.isEmpty()) {
                    etPhone.setText("+7 (9")
                }
            }

            etQuestionsAmount.addTextChangedListener {
                if (!validateQuestionsAmount(etQuestionsAmount.text.toString())) {
                    tilQuestionsAmount.error = "enter number of questions between 1 and ${Constants.MAX_QUESTIONS}"
                } else {
                    tilQuestionsAmount.error = ""
                }
                btnStart.isEnabled =
                    validateAll(etPhone.text.toString(), etQuestionsAmount.text.toString())
            }

            btnStart.setOnClickListener {
                (requireActivity() as BaseActivity).goToScreen(
                    ActionType.REPLACE,
                    QuestionnairePageFragment.newInstance(Integer.parseInt(etQuestionsAmount.text.toString())),
                    QuestionnairePageFragment.QUESTIONNAIRE_PAGE_FRAGMENT_TAG,
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
        const val START_PAGE_FRAGMENT_TAG = "START_PAGE_FRAGMENT_TAG"
    }
}
