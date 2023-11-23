package com.itis.android_tasks

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.databinding.ActivityMainBinding
import com.itis.android_tasks.ui.fragments.CoroutinesSettingsPageFragment
import com.itis.android_tasks.ui.fragments.MainPageFragment
import com.itis.android_tasks.ui.fragments.NotificationSettingsPageFragment
import com.itis.android_tasks.utils.ActionType
import com.itis.android_tasks.utils.ParamsKey
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.model.settings.CoroutinesSettings
import com.itis.android_tasks.utils.AirplaneModeChangingListener
import com.itis.android_tasks.utils.NotificationImportance
import com.itis.android_tasks.utils.PermissionRequestHandler
import com.itis.android_tasks.utils.StartCoroutinesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override val fragmentContainerId: Int = R.id.main_activity_container

    private var permissionRequestHandler: PermissionRequestHandler? = null
    private var airplaneModeChangingListener: AirplaneModeChangingListener? = null
    private var startCoroutinesManager: StartCoroutinesManager? = null //to prevent the user from changing coroutine settings after startup

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

//        supportActionBar?.let {
//            it.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//            it.setDisplayShowCustomEnabled(true)
//            it.setCustomView(R.layout.action_bar)
//        }

        airplaneModeChangingListener = AirplaneModeChangingListener(
            this,
            onAirplaneModeChanged = {
                binding.llAirplaneMode.isVisible = it
            }
        ).also {
            it.onStartAirplaneModeCheck()
        }
        initThemeListener()
        initTheme()
        initBottomNavigation()
        setUpPermissionHandler()
        initNotificationChannels()

        if (savedInstanceState == null) {
            showSelectedFragment()
        }

        if (Build.VERSION.SDK_INT >= TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(POST_NOTIFICATIONS)
        }
    }

    private fun showSelectedFragment() {
        navigate(binding.mainBnv.selectedItemId)
    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener {
            navigate(it.itemId)
        }
    }

    private fun navigate(itemId: Int): Boolean {
        return when (itemId) {
            R.id.item_main -> {
                navigateTo(
                    MainPageFragment(),
                    MainPageFragment.MAIN_PAGE_FRAGMENT_TAG
                )
                true
            }

            R.id.item_notification_settings -> {
                navigateTo(
                    NotificationSettingsPageFragment(),
                    NotificationSettingsPageFragment.NOTIFICATION_SETTINGS_PAGE_FRAGMENT_TAG
                )
                true
            }

            R.id.item_coroutine_settings -> {
                navigateTo(
                    CoroutinesSettingsPageFragment(),
                    CoroutinesSettingsPageFragment.COROUTINES_SETTINGS_PAGE_FRAGMENT_TAG
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

    fun requestPermission(permission: String) {
        permissionRequestHandler?.requestPermission(permission)
    }

    private fun setUpPermissionHandler() {
        permissionRequestHandler = PermissionRequestHandler(
            activity = this,
            rationaleCallback = {
                AlertDialog.Builder(this)
                    .setTitle(
                        getString(R.string.permission_notifications_dialog_title)
                    )
                    .setMessage(
                        getString(R.string.permission_notifications_dialog_message_rationale)
                    )
                    .setPositiveButton(
                        getString(R.string.permission_notifications_dialog_rationale_button),
                    ) { _, _ ->
                        if (Build.VERSION.SDK_INT >= TIRAMISU) {
                            requestPermission(POST_NOTIFICATIONS)
                        }
                    }.show()
            },
            deniedCallback = {
                AlertDialog.Builder(this)
                    .setTitle(
                        getString(R.string.permission_notifications_dialog_title)
                    )
                    .setMessage(
                        getString(R.string.permission_notifications_dialog_message_rationale)
                                + getString(R.string.permission_notifications_dialog_message)
                    )
                    .setPositiveButton(
                        getString(R.string.permission_notifications_dialog_button)
                    ) { _, _ ->
                        val appSettingsIntent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + this.packageName)
                        )
                        startActivity(appSettingsIntent)
                    }
                    .show()
            }
        )
    }

    private fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (importance in NotificationImportance.values()) {
                NotificationChannel(
                    ParamsKey.DEFAULT_NOTIFICATION_CHANNEL_ID + importance.name,
                    ParamsKey.DEFAULT_NOTIFICATION_CHANNEL_NAME + importance.name,
                    importance.importance
                ).also {
                    (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.createNotificationChannel(it)
                }
            }
        }
    }
    fun startCoroutines() {
        startCoroutinesManager = StartCoroutinesManager(this, CoroutinesSettings)
        job?.cancel()
        job = startCoroutinesManager?.startCoroutines()
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let {
            when (it.getString(ParamsKey.INTENT_KEY)) {
                ParamsKey.INTENT_HOME_TOAST_VALUE -> {
                    Toast.makeText(
                        this,
                        getString(R.string.welcome_back_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ParamsKey.INTENT_NOTIFICATION_SETTINGS_VALUE -> {
                    navigateTo(
                        NotificationSettingsPageFragment(),
                        NotificationSettingsPageFragment.NOTIFICATION_SETTINGS_PAGE_FRAGMENT_TAG
                    )
                }
                else -> {}
            }
        }
    }

    override fun onStop() {
        super.onStop()
        startCoroutinesManager?.let {
            if (it.isStoppedOnBackground) {
                job?.cancel()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        permissionRequestHandler = null
        this.unregisterReceiver(airplaneModeChangingListener?.receiver)
    }
}
