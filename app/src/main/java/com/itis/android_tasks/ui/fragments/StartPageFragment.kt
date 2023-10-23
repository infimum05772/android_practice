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

    private fun initViews() {
        with(binding) {
            var isCorrectPhone: Boolean = false;
            etPhone.addTextChangedListener(object : TextWatcher {

                var removeToEndIndex: Int = -1

                private fun doSetTextTransaction(
                    input: CharSequence,
                    action: TextWatcherActionType
                ) {
                    etPhone.removeTextChangedListener(this)
                    etPhone.setText(input.let {
                        if (action == TextWatcherActionType.ADD) {
                            addNumbersAutocomplete(input)
                        } else {
                            removeNumbersAutocomplete(input)
                        }
                    })
                    etPhone.setSelection(etPhone.text?.length ?: 0)
                    etPhone.addTextChangedListener(this)
                }

                private fun addNumbersAutocomplete(input: CharSequence): CharSequence {
                    if (input.isEmpty()) {
                        return "+7 (9${input}"
                    }
                    if (input.length == 7) {
                        return "${input})-"
                    }
                    if (input.length == 12 || input.length == 15) {
                        return "${input}-"
                    }
                    return input
                }

                private fun removeNumbersAutocomplete(input: CharSequence): CharSequence {
                    if (input.length == 5) {
                        return ""
                    }
                    if (input.length == 9) {
                        return input.subSequence(0, input.length - 3)
                    }
                    if (input.length == 13 || input.length == 16) {
                        return input.subSequence(0, input.length - 2)
                    }
                    return input
                }

                override fun beforeTextChanged(
                    input: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    input?.let {
                        if (etPhone.selectionEnd != etPhone.text.length) {
                            removeToEndIndex = start
                        }
                        if (start == 0) {
                            doSetTextTransaction(input, TextWatcherActionType.ADD)
                        }
                        if (after == 0) {
                            doSetTextTransaction(input, TextWatcherActionType.REMOVE)
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
                            doSetTextTransaction(input, TextWatcherActionType.ADD)
                        }
                        if (removeToEndIndex != -1) {
                            doSetTextTransaction(
                                input.removeRange(
                                    removeToEndIndex,
                                    removeToEndIndex + 1
                                ).toString() + input.subSequence(
                                    removeToEndIndex,
                                    removeToEndIndex + 1
                                ), TextWatcherActionType.ADD
                            )
                            removeToEndIndex = -1
                        }
                    }
                }

                override fun afterTextChanged(input: Editable?) {
                    input?.let {
                        isCorrectPhone = input.length == 18
                        if (isCorrectPhone) {
                            tilPhone.error = ""
                        }
                    }
                }
            })

            etPhone.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    if (!isCorrectPhone) {
                        tilPhone.error = "Phone number must contain 10 digits"
                    }
                } else if (etPhone.text.isEmpty()) {
                    etPhone.setText("+7 (9")
                }
            }

            etQuestionsAmount.addTextChangedListener {
                if (etQuestionsAmount.text.isEmpty() || etQuestionsAmount.text[0] == '0' || Integer.parseInt(
                        etQuestionsAmount.text.toString()
                    ) > 30
                ) {
                    tilQuestionsAmount.error = "enter number of questions between 1 and 30"
                } else {
                    tilQuestionsAmount.error = ""
                }
            }
            btnStart.setOnClickListener {

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
