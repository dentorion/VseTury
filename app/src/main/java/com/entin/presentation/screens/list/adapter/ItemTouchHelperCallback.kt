package com.entin.presentation.screens.list.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.RecyclerView
import com.entin.presentation.model.TourDomainModel

class ItemTouchHelperCallback(
    private val adapter: ToursAdapter?,
    private val swipeCallback: (tour: TourDomainModel) -> Unit,
) : ItemTouchHelper.SimpleCallback(0, START or END) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter?.let {
            swipeCallback(
                it.currentList[viewHolder.absoluteAdapterPosition]
            )
        }
    }
}