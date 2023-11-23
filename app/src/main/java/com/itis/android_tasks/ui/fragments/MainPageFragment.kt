package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentMainPageBinding
import com.itis.android_tasks.model.settings.NotificationSettings
import com.itis.android_tasks.utils.AirplaneModeChangingListener

class MainPageFragment : Fragment(R.layout.fragment_main_page) {

    private var _binding: FragmentMainPageBinding? = null
    private val binding: FragmentMainPageBinding
        get() = _binding!!

    private val notificationSettings: NotificationSettings = NotificationSettings
    private var airplaneModeChangingListener: AirplaneModeChangingListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            airplaneModeChangingListener = AirplaneModeChangingListener(
                requireContext(),
                onAirplaneModeChanged = {
                    binding.btnShowNotification.isEnabled = !it
                }
            ).also {
                it.onStartAirplaneModeCheck()
            }

            etNotificationTitle.setText(notificationSettings.title)
            etNotificationDescription.setText(notificationSettings.description)
        }
        initTitleChangedListener()
        initDescriptionChangedListener()
    }

    private fun initTitleChangedListener() {
        binding.etNotificationTitle.addTextChangedListener {
            notificationSettings.title = it.toString()
        }
    }

    private fun initDescriptionChangedListener() {
        binding.etNotificationDescription.addTextChangedListener {
            notificationSettings.description = it.toString()
        }
    }

    override fun onDestroyView() {
        _binding = null
        requireContext().unregisterReceiver(airplaneModeChangingListener?.receiver)
        super.onDestroyView()
    }

    companion object {
        const val MAIN_PAGE_FRAGMENT_TAG = "MAIN_PAGE_FRAGMENT_TAG"
    }
}
