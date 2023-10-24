package com.itis.android_tasks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.itis.android_tasks.model.Question
import com.itis.android_tasks.ui.fragments.QuestionPageFragment

class QuestionnaireAdapter(
    var questions: List<Question>,
    manager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int {
        return questions.size + 2
    }

    override fun createFragment(position: Int): Fragment {
        val realPos = if (position == 0) questions.size - 1 else (position - 1) % questions.size
        return QuestionPageFragment.newInstance(questions[realPos], realPos)
    }
}
