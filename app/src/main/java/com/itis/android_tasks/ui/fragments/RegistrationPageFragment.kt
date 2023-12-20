package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentRegistrationPageBinding
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.TextWatcherActionType

class RegistrationPageFragment : Fragment(R.layout.fragment_registration_page) {

    private var _binding: FragmentRegistrationPageBinding? = null
    private val binding: FragmentRegistrationPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun validatePhone(phone: String): Boolean {
        return phone.matches(Regex(getString(R.string.phone_regex)))
    }

    private fun validateEmail(email: String): Boolean {
        return email.matches(Regex(getString(R.string.email_regex)))
    }

    private fun validatePassword(password: String): Boolean {
        return password.matches(Regex(getString(R.string.password_regex)))
    }

    private fun checkPassword(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }

    private fun validateAll(): Boolean {
        with(binding) {
            return (!etNameInput.text.isNullOrEmpty() &&
                    validatePhone(etPhoneInput.text.toString()) &&
                    validateEmail(etEmailInput.text.toString()) &&
                    validatePassword(etPasswordInput.text.toString()) &&
                    checkPassword(
                        etPasswordInput.text.toString(),
                        etCheckPasswordInput.text.toString()
                    )).also {
                btnRegister.isEnabled = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        addNameValidation()
        addPhoneAutocompletion()
        addEmailValidation()
        addPasswordValidation()
        addCheckPasswordValidation()

        with(binding) {
            btnRegister.setOnClickListener {
                (requireActivity() as? BaseActivity)?.goToScreen(
                    ActionType.REPLACE,
                    AuthorizationPageFragment.newInstance(
                        etEmailInput.text.toString(),
                        etPasswordInput.text.toString()
                    ),
                    AuthorizationPageFragment.AUTHORIZATION_PAGE_FRAGMENT_TAG,
                    false
                )
            }
        }
    }

    private fun addNameValidation() {
        with(binding) {
            etNameInput.addTextChangedListener { input ->
                if (!input.isNullOrEmpty()) {
                    tilName.error = ""
                }
                validateAll()
            }

            etNameInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused && etNameInput.text.toString().isEmpty()) {
                    tilName.error = getString(R.string.empty_name_error)
                }
            }
        }
    }

    private fun addPhoneAutocompletion() {
        with(binding) {
            etPhoneInput.addTextChangedListener(object : TextWatcher {

                private fun doSetTextTransaction(
                    input: CharSequence,
                    action: TextWatcherActionType,
                    selection: Int,
                    count: Int
                ) {
                    etPhoneInput.removeTextChangedListener(this)
                    etPhoneInput.setText(input.let {
                        when (action) {
                            TextWatcherActionType.ADD -> {
                                addNumbersAutocomplete(input, selection, count)
                            }

                            TextWatcherActionType.REMOVE -> {
                                removeNumbersAutocomplete(input)
                            }
                        }
                    })
                    etPhoneInput.setSelection(etPhoneInput.text?.length ?: 0)
                    etPhoneInput.addTextChangedListener(this)
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
                        return getString(R.string.phone_second_part, inputModified)
                    }
                    if (input.length == 12 || input.length == 15) {
                        return getString(R.string.phone_end_parts, inputModified)
                    }
                    return inputModified
                }

                private fun removeNumbersAutocomplete(
                    input: CharSequence
                ): CharSequence {
                    if (input.length <= 5) {
                        return getString(R.string.phone_start_part)
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
                        validateAll()
                    }
                }
            })

            etPhoneInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    if (!etPhoneInput.text.toString()
                            .matches(Regex(getString(R.string.phone_regex_incomplete)))
                    ) {
                        tilPhone.error = getString(R.string.phone_does_not_match_mask_error)
                    } else if (!validatePhone(etPhoneInput.text.toString())) {
                        tilPhone.error = getString(R.string.phone_incomplete_error)
                    }
                } else if (etPhoneInput.text.isEmpty()) {
                    etPhoneInput.setText(getString(R.string.phone_start_part))
                }
            }
        }
    }

    private fun addEmailValidation() {
        with(binding) {
            etEmailInput.addTextChangedListener { input ->
                if (validateEmail(input.toString())) {
                    tilEmail.error = ""
                }
                validateAll()
            }

            etEmailInput.setOnFocusChangeListener { _, isFocused ->
                val emailInput = etEmailInput.text.toString()
                if (!isFocused) {
                    if (emailInput.isEmpty()) {
                        tilEmail.error = getString(R.string.empty_email_error)
                    } else if (!validateEmail(emailInput)) {
                        tilEmail.error = getString(R.string.invalid_email_error)
                    }
                }
            }
        }
    }

    private fun addPasswordValidation() {
        with(binding) {
            etPasswordInput.addTextChangedListener { input ->
                if (validatePassword(input.toString())) {
                    tilPassword.error = ""
                }
                validateAll()
            }

            etPasswordInput.setOnFocusChangeListener { _, isFocused ->
                val passwordInput = etPasswordInput.text.toString()
                if (!isFocused) {
                    if (passwordInput.isEmpty()) {
                        tilPassword.error = getString(R.string.empty_password_error)
                    } else if (passwordInput.length < 8) {
                        tilPassword.error = getString(R.string.short_password_error)
                    } else if (!validatePassword(passwordInput)) {
                        tilPassword.error = getString(R.string.weak_password_error)
                    }
                }
            }
        }
    }

    private fun addCheckPasswordValidation() {
        with(binding) {
            etCheckPasswordInput.addTextChangedListener { input ->
                if (checkPassword(etPasswordInput.text.toString(), input.toString())) {
                    tilCheckPassword.error = ""
                }
                validateAll()
            }

            etCheckPasswordInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused &&
                    !checkPassword(
                        etPasswordInput.text.toString(),
                        etCheckPasswordInput.text.toString()
                    )
                ) {
                    tilCheckPassword.error = getString(R.string.password_does_not_match_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val REGISTRATION_PAGE_FRAGMENT_TAG = "REGISTRATION_PAGE_FRAGMENT_TAG"
    }
}
