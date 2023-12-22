package com.itis.android_tasks.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.R
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.ActivityMainBinding
import com.itis.android_tasks.di.ServiceLocator
import com.itis.android_tasks.service.impl.UserServiceImpl
import com.itis.android_tasks.session.AppSession
import com.itis.android_tasks.ui.fragments.AccountPageFragment
import com.itis.android_tasks.ui.fragments.AddDataPageFragment
import com.itis.android_tasks.ui.fragments.AuthorizationPageFragment
import com.itis.android_tasks.ui.fragments.MainPageFragment
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val sessionEmail = ServiceLocator.getSharedPreferences()
            .getString(ParamsKey.EMAIL_SP_KEY, null)

        initBottomNavigation()
        binding.mainBnv.isVisible = sessionEmail != null

        if (savedInstanceState == null) {
            if (sessionEmail == null) {
                toAuthorizationPage()
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    AppSession.init(sessionEmail)
                    AppSession.saveSession()
                }
                toFeedPage()
            }
        }
    }

   fun toAuthorizationPage() {
        navigateTo(
            AuthorizationPageFragment(),
            AuthorizationPageFragment.AUTHORIZATION_PAGE_FRAGMENT_TAG
        )
        binding.mainBnv.visibility = View.GONE
    }

    fun toFeedPage() {
        binding.mainBnv.selectedItemId = R.id.item_feed
        binding.mainBnv.visibility = View.VISIBLE
    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener {
            navigate(it.itemId)
        }
    }

    private fun navigate(itemId: Int): Boolean {
        return when (itemId) {
            R.id.item_feed -> {
                navigateTo(
                    MainPageFragment(),
                    MainPageFragment.MAIN_PAGE_FRAGMENT_TAG
                )
                true
            }

            R.id.item_account -> {
                navigateTo(
                    AccountPageFragment(),
                    AccountPageFragment.ACCOUNT_PAGE_FRAGMENT_TAG
                )
                true
            }

            R.id.item_add_data -> {
                navigateTo(
                    AddDataPageFragment(),
                    AddDataPageFragment.ADD_DATA_PAGE_FRAGMENT_TAG
                )
                true
            }

            else -> false
        }
    }

    private fun navigateTo(fragment: Fragment, fragmentTag: String) {
        goToScreen(
            ActionType.REPLACE,
            fragment,
            fragmentTag,
            false
        )
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
        ServiceLocator.getSharedPreferences().edit().apply {
            putBoolean(ParamsKey.DARK_THEME, binding.switchTheme.isChecked)
            apply()
        }
    }

    private fun initTheme() {
        with(binding) {
            if (ServiceLocator.getSharedPreferences().getBoolean(
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
