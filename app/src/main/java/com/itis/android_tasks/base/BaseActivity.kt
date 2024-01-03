package com.itis.android_tasks.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.itis.android_tasks.utils.ActionType

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val fragmentContainerId: Int

    abstract fun goToScreen(
        actionType: ActionType,
        destination: Fragment,
        tag: String? = null,
        isAddToBackStack: Boolean = true,
    )
}
