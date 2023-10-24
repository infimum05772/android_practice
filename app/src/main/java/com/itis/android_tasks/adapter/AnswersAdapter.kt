package com.itis.android_tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemAnswerBinding
import com.itis.android_tasks.model.Answer
import com.itis.android_tasks.ui.holder.AnswerItem

class AnswersAdapter(
    private var answers: List<Answer>,
) : RecyclerView.Adapter<AnswerItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerItem = AnswerItem(
        ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int {
        return answers.size
    }

    override fun onBindViewHolder(holder: AnswerItem, position: Int) {
        holder.onBind(answers[position])
    }
}
