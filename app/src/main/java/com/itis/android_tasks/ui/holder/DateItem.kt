package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemDateBinding
import com.itis.android_tasks.model.DateModel
import java.time.LocalDate

class DateItem(
    private val viewBinding: ItemDateBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindItem(date: DateModel) {
        with(viewBinding) {
            tvDate.text = date.toString()
        }
    }
}
