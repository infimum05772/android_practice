package com.itis.android_tasks.adapter.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridHorizontalDecorator(
    private val itemOffset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams = view.layoutParams as? GridLayoutManager.LayoutParams

        with(outRect) {
            layoutParams?.let { params ->
                if (params.spanIndex % 2 == 0) {
                    left = itemOffset
                    right = itemOffset / 2
                } else {
                    right = itemOffset
                    left = itemOffset / 2
                }
            }
        }
    }
}
