package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentCoroutinesSettingsPageBinding
import com.itis.android_tasks.model.settings.CoroutinesSettings
import com.itis.android_tasks.utils.AirplaneModeChangingListener

class CoroutinesSettingsPageFragment : Fragment(R.layout.fragment_coroutines_settings_page) {

    private var _binding: FragmentCoroutinesSettingsPageBinding? = null
    private val binding: FragmentCoroutinesSettingsPageBinding
        get() = _binding!!

    private val coroutinesSettings: CoroutinesSettings = CoroutinesSettings

    private var airplaneModeChangingListener: AirplaneModeChangingListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoroutinesSettingsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        airplaneModeChangingListener = AirplaneModeChangingListener(
            requireContext(),
            onAirplaneModeChanged = {
                binding.btnStartCoroutines.isEnabled = !it
            }
        ).also {
            it.onStartAirplaneModeCheck()
        }

        initCoroutinesSettings()
        initCoroutinesCountChangedListener()
        initIsAsyncStateChangedListener()
        initIsStoppedOnBackgroundStateChangedListener()

        binding.btnStartCoroutines.setOnClickListener {

        }
    }

    private fun initCoroutinesSettings() {
        with(binding) {
            sbCoroutinesCount.progress = coroutinesSettings.count
            tvCoroutineCountProgress.text = coroutinesSettings.count.toString()
            chbAsync.isChecked = coroutinesSettings.isAsync
            chbStopOnBg.isChecked = coroutinesSettings.isStoppedOnBackground
        }
    }

    private fun initCoroutinesCountChangedListener() {
        with(binding) {
            sbCoroutinesCount.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    var trueProgress = progress
                    if (progress < 1){
                        seekBar?.progress = 1
                        trueProgress = 1
                    }
                    tvCoroutineCountProgress.text = trueProgress.toString()
                    coroutinesSettings.count = trueProgress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }
    }

    private fun initIsAsyncStateChangedListener() {
        binding.chbAsync.setOnCheckedChangeListener { _, isChecked ->
            coroutinesSettings.isAsync = isChecked
        }
    }

    private fun initIsStoppedOnBackgroundStateChangedListener() {
        binding.chbStopOnBg.setOnCheckedChangeListener { _, isChecked ->
            coroutinesSettings.isStoppedOnBackground = isChecked
        }
    }

    override fun onDestroyView() {
        _binding = null
        requireContext().unregisterReceiver(airplaneModeChangingListener?.receiver)
        super.onDestroyView()
    }

    companion object {
        const val COROUTINES_SETTINGS_PAGE_FRAGMENT_TAG = "COROUTINES_SETTINGS_PAGE_FRAGMENT_TAG"
    }
}
