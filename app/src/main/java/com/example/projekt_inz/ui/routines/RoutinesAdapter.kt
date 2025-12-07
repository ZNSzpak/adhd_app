package com.example.projekt_inz.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.todolist.TaskEntity

class RoutinesAdapter(
    private val onClick: (ButtonListEntry) -> Unit,
    private val onEditClick: (ButtonListEntry) -> Unit,
    private val onDeleteClick: (ButtonListEntry) -> Unit
) : ListAdapter<ButtonListEntry, RoutinesViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutinesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.button_routine, parent, false)
        return RoutinesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutinesViewHolder, position: Int) {
        val item = getItem(position)
        holder.button.text = item.title
        holder.button.setOnClickListener { onClick(item) }
        holder.editButton.setOnClickListener { onEditClick(item) }
        holder.deleteButton.setOnClickListener { onDeleteClick(item) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ButtonListEntry>() {
        override fun areItemsTheSame(oldItem: ButtonListEntry, newItem: ButtonListEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ButtonListEntry, newItem: ButtonListEntry): Boolean =
            oldItem == newItem
    }
}