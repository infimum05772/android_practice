package com.itis.android_tasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.FragmentFirstPageBinding
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.DataListener
import com.itis.android_tasks.utils.ParamsKey

class FirstPageFragment : Fragment(R.layout.fragment_first_page) {

    private var _binding: FragmentFirstPageBinding? = null
    private val binding: FragmentFirstPageBinding
        get() = _binding!!
    private lateinit var dataListener: DataListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            btnSubmit.setOnClickListener {
                (requireActivity() as BaseActivity).goToScreen(
                    ActionType.REPLACE,
                    SecondPageFragment.newInstance(etTextToDisplay.text.toString()),
                    SecondPageFragment.SECOND_PAGE_FRAGMENT_TAG,
                    true
                )
                (requireActivity() as BaseActivity).goToScreen(
                    ActionType.REPLACE,
                    ThirdPageFragment.newInstance(etTextToDisplay.text.toString()),
                    ThirdPageFragment.THIRD_PAGE_FRAGMENT_TAG,
                    true
                )
            }
            btnSave.setOnClickListener {
                dataListener =
                    requireActivity().supportFragmentManager.findFragmentByTag(FourthPageFragment.FOURTH_PAGE_FRAGMENT_TAG) as DataListener
                sendData(etTextToDisplay.text.toString())
                etTextToDisplay.text = null
            }
        }
    }

    private fun sendData(message: String) {
        if (message.isNullOrEmpty()) {
            Toast.makeText(context, "enter some text to display", Toast.LENGTH_SHORT).show()
            return
        }
        dataListener.onDataReceived(message)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val FIRST_PAGE_FRAGMENT_TAG = "FIRST_PAGE_FRAGMENT_TAG"
    }
}
