package com.itis.android_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.itis.android_tasks.base.BaseActivity
import com.itis.android_tasks.databinding.ActivityMainBinding
import com.itis.android_tasks.ui.FirstPageFragment
import com.itis.android_tasks.ui.MainFragment
import com.itis.android_tasks.utils.ActionType

class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override val fragmentContainerId: Int = R.id.main_activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(R.layout.activity_main)
        }

        if (supportFragmentManager.findFragmentByTag(MainFragment.MAIN_FRAGMENT_TAG) == null) {
            goToScreen(
                ActionType.ADD,
                MainFragment(),
                MainFragment.MAIN_FRAGMENT_TAG,
                false
            )
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }
}
