package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.itis.android_tasks.databinding.FragmentNotificationSettingsPageBinding
import com.itis.android_tasks.model.settings.NotificationSettings
import com.itis.android_tasks.utils.NotificationImportance
import com.itis.android_tasks.utils.NotificationPrivacy

class NotificationSettingsPageFragment : Fragment() {
    private var _binding: FragmentNotificationSettingsPageBinding? = null
    private val binding: FragmentNotificationSettingsPageBinding
        get() = _binding!!

    private val notificationSettings: NotificationSettings = NotificationSettings

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationSettingsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        initNotificationSettings()
        initImportanceChangedListener()
        initPrivacyChangedListener()
        initIsExpandableTextStateChangedListener()
        initIsButtonsShowedStateChangedListener()
    }

    private fun initNotificationSettings() {
        with(binding) {
            spinnerNotificationsImportance.setSelection(notificationSettings.importance.ordinal)
            spinnerNotificationsPrivacy.setSelection(notificationSettings.privacy.ordinal)
            chbExpandableNotificationText.isChecked = notificationSettings.isExpandableText
            chbShowDialogButtons.isChecked = notificationSettings.isButtonsShowed
        }
    }

    private fun initImportanceChangedListener() {
        binding.spinnerNotificationsImportance.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    notificationSettings.importance = NotificationImportance.values()[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }

    private fun initPrivacyChangedListener() {
        binding.spinnerNotificationsPrivacy.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    notificationSettings.privacy = NotificationPrivacy.values()[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }

    private fun initIsExpandableTextStateChangedListener() {
        binding.chbExpandableNotificationText.setOnCheckedChangeListener { _, isChecked ->
            notificationSettings.isExpandableText = isChecked
        }
    }

    private fun initIsButtonsShowedStateChangedListener() {
        binding.chbShowDialogButtons.setOnCheckedChangeListener { _, isChecked ->
            notificationSettings.isButtonsShowed = isChecked
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val NOTIFICATION_SETTINGS_PAGE_FRAGMENT_TAG =
            "NOTIFICATION_SETTINGS_PAGE_FRAGMENT_TAG"
    }
}
