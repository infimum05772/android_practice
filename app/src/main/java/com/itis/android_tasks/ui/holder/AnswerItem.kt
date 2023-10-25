package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.itis.android_tasks.databinding.ItemAnswerBinding
import com.itis.android_tasks.model.Answer

class AnswerItem(
    private val binding: ItemAnswerBinding,
    private val onCheckedChange: (Int) -> Unit
) : ViewHolder(binding.root) {

    init {
        binding.rbAnswer.setOnClickListener {
            onCheckedChange.invoke(adapterPosition)
        }
    }

    fun onBind(answer: Answer) {
        binding.run {
            rbAnswer.apply {
                text = answer.text
                isChecked = answer.isChecked
                isEnabled = !answer.isChecked
            }
        }
    }
}
