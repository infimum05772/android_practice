package com.itis.android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.android_tasks.databinding.ItemAddButtonBinding

class AddButtonItem(
    private val viewBinding: ItemAddButtonBinding,
    private val onAddButtonClicked: () -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    init {
        viewBinding.btnOpenDialog.setOnClickListener {
            onAddButtonClicked.invoke()
        }
    }
}
