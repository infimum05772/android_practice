package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentAccountPageBinding
import com.itis.android_tasks.service.impl.UserServiceImpl
import com.itis.android_tasks.session.AppSession
import com.itis.android_tasks.ui.MainActivity
import com.itis.android_tasks.utils.PhoneAutocompleteUtil
import com.itis.android_tasks.utils.TextWatcherActionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountPageFragment : Fragment(R.layout.fragment_account_page) {

    private var _binding: FragmentAccountPageBinding? = null
    private val binding: FragmentAccountPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        with(binding) {
            addPhoneAutocompletion()
            addNewPasswordValidation()
            addRepeatPasswordValidation()
            addCurrentPasswordValidation()

            AppSession.getCurrentUser()?.let { currUser ->
                tvName.text = currUser.name
                tvEmail.text = currUser.email
                etPhoneInput.setText(currUser.phone)
            }

            btnSavePhone.setOnClickListener {
                savePhone()
            }

            btnSavePassword.setOnClickListener {
                savePassword()
            }

            btnLogOut.setOnClickListener {
                logOut()
            }

            btnDeleteAccount.setOnClickListener {
                deleteAccAlertDialog()
            }
        }
    }

    private fun deleteAccAlertDialog() {
        AppSession.getCurrentUser()?.let { currUser ->
            context?.let { ctx ->
                AlertDialog.Builder(ctx)
                    .setTitle(
                        getString(R.string.acc_deleting_dialog_header)
                    )
                    .setMessage(
                        getString(R.string.acc_deleting_dialog_text)
                    )
                    .setPositiveButton(
                        getString(R.string.acc_deleting_dialog_positive_button),
                    ) { _, _ ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            UserServiceImpl.deleteUser(currUser.email)
                            requireActivity().runOnUiThread {
                                logOut()
                            }
                        }
                    }
                    .setNegativeButton(
                        getString(R.string.acc_deleting_dialog_negative_button)
                    ) { _, _ -> }.show()
            }
        }
    }

    private fun savePassword() {
        with(binding) {
            AppSession.getCurrentUser()?.let { currUser ->
                val currPassword = etCurrentPasswordInput.text.toString()
                val newPassword = etNewPasswordInput.text.toString()
                tilCurrentPassword.error = ""
                lifecycleScope.launch(Dispatchers.IO) {
                    if (UserServiceImpl.updatePassword(currUser.email, currPassword, newPassword)) {
                        AppSession.updateUser()
                        showSuccessToast()
                    } else {
                        requireActivity().runOnUiThread {
                            tilCurrentPassword.error = getString(R.string.wrong_password_error)
                        }
                    }
                }
            }
        }
    }

    private fun savePhone() {
        with(binding) {
            AppSession.getCurrentUser()?.let { currUser ->
                val phone = etPhoneInput.text.toString()
                if (phone != currUser.phone) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (UserServiceImpl.updatePhone(currUser.email, phone)) {
                            AppSession.updateUser()
                            showSuccessToast()
                        } else {
                            requireActivity().runOnUiThread {
                                tilPhone.error = getString(R.string.not_unique_phone_error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessToast() {
        requireActivity().runOnUiThread {
            Toast.makeText(
                context,
                getString(R.string.changing_success_msg),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun logOut() {
        AppSession.removeUser()
        AppSession.saveSession()
        (requireActivity() as? MainActivity)?.toAuthorizationPage()
    }

    private fun validatePhone(phone: String): Boolean {
        return phone.matches(Regex(getString(R.string.phone_regex)))
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
                                PhoneAutocompleteUtil.addNumbersAutocomplete(
                                    input,
                                    selection,
                                    count
                                )
                            }

                            TextWatcherActionType.REMOVE -> {
                                PhoneAutocompleteUtil.removeNumbersAutocomplete(input)
                            }
                        }
                    })
                    etPhoneInput.setSelection(etPhoneInput.text?.length ?: 0)
                    etPhoneInput.addTextChangedListener(this)
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
                        btnSavePhone.isEnabled = validatePhone(input.toString()).also {
                            if (!it) {
                                tilPhone.error = getString(R.string.phone_does_not_match_mask_error)
                            } else {
                                tilPhone.error = ""
                            }
                        }
                    }
                }
            })
        }
    }

    private fun validateNewPassword(password: String): Boolean {
        return password.matches(Regex(getString(R.string.password_regex)))
    }

    private fun checkPassword(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }

    private fun validatePasswords(): Boolean {
        with(binding) {
            return (validateNewPassword(etNewPasswordInput.text.toString()) &&
                    checkPassword(
                        etNewPasswordInput.text.toString(),
                        etRepeatPasswordInput.text.toString()
                    ) &&
                    etCurrentPasswordInput.text.isNotEmpty()
                    ).also {
                    btnSavePassword.isEnabled = it
                }
        }
    }

    private fun addCurrentPasswordValidation() {
        with(binding) {
            etCurrentPasswordInput.addTextChangedListener { input ->
                if (input.isNullOrEmpty()) {
                    tilCurrentPassword.error = ""
                }
                validatePasswords()
            }

            etCurrentPasswordInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    if (etCurrentPasswordInput.text.isEmpty()) {
                        tilCurrentPassword.error = getString(R.string.empty_password_error)
                    }
                }
            }
        }
    }

    private fun addNewPasswordValidation() {
        with(binding) {
            etNewPasswordInput.addTextChangedListener { input ->
                if (validateNewPassword(input.toString())) {
                    tilNewPassword.error = ""
                }
                validatePasswords()
            }

            etNewPasswordInput.setOnFocusChangeListener { _, isFocused ->
                val passwordInput = etNewPasswordInput.text.toString()
                if (!isFocused) {
                    if (passwordInput.isEmpty()) {
                        tilNewPassword.error = getString(R.string.empty_password_error)
                    } else if (passwordInput.length < 8) {
                        tilNewPassword.error = getString(R.string.short_password_error)
                    } else if (!validateNewPassword(passwordInput)) {
                        tilNewPassword.error = getString(R.string.weak_password_error)
                    }
                }
            }
        }
    }

    private fun addRepeatPasswordValidation() {
        with(binding) {
            etRepeatPasswordInput.addTextChangedListener { input ->
                if (checkPassword(etNewPasswordInput.text.toString(), input.toString())) {
                    tilRepeatPassword.error = ""
                }
                validatePasswords()
            }

            etRepeatPasswordInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused &&
                    !checkPassword(
                        etNewPasswordInput.text.toString(),
                        etRepeatPasswordInput.text.toString()
                    )
                ) {
                    tilRepeatPassword.error = getString(R.string.password_does_not_match_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ACCOUNT_PAGE_FRAGMENT_TAG = "ACCOUNT_PAGE_FRAGMENT_TAG"
    }
}
