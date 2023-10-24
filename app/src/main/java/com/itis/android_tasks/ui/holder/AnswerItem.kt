package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.itis.android_tasks.databinding.ItemAnswerBinding
import com.itis.android_tasks.model.Answer

class AnswerItem(
    private val binding: ItemAnswerBinding
) : ViewHolder(binding.root) {

    fun onBind(answer: Answer) {
        binding.run {
            rbAnswer.text = answer.text
            rbAnswer.isChecked = answer.isChecked
        }
    }
}
