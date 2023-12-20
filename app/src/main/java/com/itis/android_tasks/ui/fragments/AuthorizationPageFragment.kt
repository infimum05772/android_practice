package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentAuthorizationPageBinding
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.ParamsKey

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

            }

            arguments?.let {
                val email = it.getString(ParamsKey.EMAIL_KEY)
                val password = it.getString(ParamsKey.PASSWORD_KEY)
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val AUTHORIZATION_PAGE_FRAGMENT_TAG = "AUTHORIZATION_PAGE_FRAGMENT_TAG"

        fun newInstance(email: String, password: String) = AuthorizationPageFragment().apply {
            arguments = bundleOf(ParamsKey.EMAIL_KEY to email, ParamsKey.PASSWORD_KEY to password)
        }
    }
}
