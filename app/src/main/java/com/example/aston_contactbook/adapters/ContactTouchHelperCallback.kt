package com.example.aston_contactbook.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_contactbook.MainActivity

class ContactTouchHelperCallback(private val adapter: ContactAdapter) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        0
    ) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        adapter.swapItems(fromPosition, toPosition)
        (recyclerView.adapter as? ContactAdapter)?.getCurrentList()?.let { updatedList ->
            (recyclerView.context as? MainActivity)?.updateContacts(updatedList)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Not used
    }
}