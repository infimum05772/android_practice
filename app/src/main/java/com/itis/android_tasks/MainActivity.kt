package com.itis.android_tasks

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.ActivityMainBinding
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.ParamsKey

class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override val fragmentContainerId: Int = R.id.main_activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initThemeListener()
        initTheme()
//        if (supportFragmentManager.findFragmentByTag(StartPageFragment.START_PAGE_FRAGMENT_TAG) == null) {
//            goToScreen(
//                ActionType.ADD,
//                StartPageFragment(),
//                StartPageFragment.START_PAGE_FRAGMENT_TAG,
//                false
//             )
//        }
    }

    override fun goToScreen(
        actionType: ActionType,
        destination: Fragment,
        tag: String?,
        isAddToBackStack: Boolean,
    ) {
        supportFragmentManager.beginTransaction().apply {
            when (actionType) {
                ActionType.ADD -> {
                    this.add(fragmentContainerId, destination, tag)
                }

                ActionType.REPLACE -> {
                    this.replace(fragmentContainerId, destination, tag)
                }

                ActionType.REMOVE -> {
                    this.remove(destination)
                }
            }
            if (isAddToBackStack) {
                this.addToBackStack(null)
            }
        }.commit()
    }

    private fun saveTheme() {
        getSharedPreferences(ParamsKey.APP_CONFIG, Context.MODE_PRIVATE).edit().apply {
            putBoolean(ParamsKey.DARK_THEME, binding.switchTheme.isChecked)
            apply()
        }
    }

    private fun initTheme() {
        with(binding) {
            if (getSharedPreferences(ParamsKey.APP_CONFIG, Context.MODE_PRIVATE).getBoolean(
                    ParamsKey.DARK_THEME,
                    false
                )
            ) {
                switchTheme.isChecked = true
                switchTheme.setThumbResource(R.drawable.light_mode_icon)
            } else {
                switchTheme.isChecked = false
                switchTheme.setThumbResource(R.drawable.dark_mode_icon)
            }
        }
    }

    private fun initThemeListener() {
        with(binding) {
            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    switchTheme.setThumbResource(R.drawable.light_mode_icon)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    switchTheme.setThumbResource(R.drawable.dark_mode_icon)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                saveTheme()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }
}
