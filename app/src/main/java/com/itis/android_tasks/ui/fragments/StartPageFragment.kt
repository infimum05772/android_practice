package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.databinding.FragmentStartPageBinding
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.TextWatcherActionType

class StartPageFragment : Fragment(R.layout.fragment_start_page) {

    private var _binding: FragmentStartPageBinding? = null
    private val binding: FragmentStartPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun validatePhone(phone: String): Boolean {
        return phone.length == 18
    }

    private fun validateQuestionsAmount(questionsAmount: String): Boolean {
        return !(questionsAmount.isEmpty() || questionsAmount[0] == '0' || Integer.parseInt(
            questionsAmount
        ) > 30)
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
                    selection: Int
                ) {
                    etPhone.removeTextChangedListener(this)
                    etPhone.setText(input.let {
                        when (action) {
                            TextWatcherActionType.ADD -> {
                                addNumbersAutocomplete(input, selection)
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
                    selection: Int
                ): CharSequence {
                    var inputModified = input
                    if (input.toString() == "+7 (9") {
                        return input
                    }
                    if (selection != input.length) {
                        input.subSequence(selection, selection + 1).let {
                            inputModified = input.removeRange(selection, selection + 1)
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
                            doSetTextTransaction(input, TextWatcherActionType.REMOVE, start)
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
                            doSetTextTransaction(input, TextWatcherActionType.ADD, start)
                        }
                    }
                }

                override fun afterTextChanged(input: Editable?) {
                    input?.let {
                        if (validatePhone(input.toString())) {
                            tilPhone.error = ""
                        }
                    }
                }
            })

            etPhone.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    if (!validatePhone(etPhone.text.toString())) {
                        tilPhone.error = "Phone number must contain 10 digits"
                    }
                } else if (etPhone.text.isEmpty()) {
                    etPhone.setText("+7 (9")
                }
                btnStart.isEnabled =
                    validateAll(etPhone.text.toString(), etQuestionsAmount.text.toString())
            }

            etQuestionsAmount.addTextChangedListener {
                if (!validateQuestionsAmount(etQuestionsAmount.text.toString())) {
                    tilQuestionsAmount.error = "enter number of questions between 1 and 30"
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
                    false
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
