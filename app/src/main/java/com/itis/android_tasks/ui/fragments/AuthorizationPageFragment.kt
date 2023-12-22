package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentAuthorizationPageBinding
import com.itis.android_tasks.service.impl.UserServiceImpl
import com.itis.android_tasks.session.AppSession
import com.itis.android_tasks.ui.MainActivity
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizationPageFragment : Fragment(R.layout.fragment_authorization_page) {

    private var _binding: FragmentAuthorizationPageBinding? = null
    private val binding: FragmentAuthorizationPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun validateInputFields(): Boolean {
        with(binding) {
            return !etEmailInput.text.isNullOrEmpty() && !etPasswordInput.text.isNullOrEmpty()
        }
    }


    private fun initViews() {
        with(binding) {
            etEmailInput.addTextChangedListener {
                btnSubmit.isEnabled = validateInputFields()
            }
            etPasswordInput.addTextChangedListener {
                btnSubmit.isEnabled = validateInputFields()
            }

            btnSubmit.setOnClickListener {
                authoriseUser()
            }

            arguments?.let {
                val email = it.getString(ParamsKey.EMAIL_BUNDLE_KEY)
                val password = it.getString(ParamsKey.PASSWORD_BUNDLE_KEY)
                if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    etEmailInput.setText(email)
                    etPasswordInput.setText(password)
                }
            }

            btnToRegistrationPage.setOnClickListener {
                (requireActivity() as? BaseActivity)?.goToScreen(
                    ActionType.REPLACE,
                    RegistrationPageFragment(),
                    RegistrationPageFragment.REGISTRATION_PAGE_FRAGMENT_TAG,
                    true
                )
            }
        }
    }

    private fun authoriseUser() {
        with(binding) {
            val email = etEmailInput.text.toString()
            val password = etPasswordInput.text.toString()
            tilEmail.error = ""
            tilPassword.error = ""
            lifecycleScope.launch(Dispatchers.IO) {
                val isRegistered = UserServiceImpl.isRegistered(
                    email,
                    password
                )
                if (isRegistered == null) {
                    requireActivity().runOnUiThread {
                        tilEmail.error = getString(R.string.unknown_user_error)
                    }
                } else if (!isRegistered) {
                    requireActivity().runOnUiThread {
                        tilPassword.error = getString(R.string.wrong_password_error)
                    }
                } else {
                    AppSession.init(email)
                    AppSession.saveSession()
                    with(requireActivity()) {
                        runOnUiThread {
                            (this as? MainActivity)?.toFeedPage()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val AUTHORIZATION_PAGE_FRAGMENT_TAG = "AUTHORIZATION_PAGE_FRAGMENT_TAG"

        fun newInstance(email: String?, password: String?) = AuthorizationPageFragment().apply {
            arguments = bundleOf(
                ParamsKey.EMAIL_BUNDLE_KEY to email,
                ParamsKey.PASSWORD_BUNDLE_KEY to password
            )
        }
    }
}
