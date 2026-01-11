package com.example.projekt_inz.ui.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.projekt_inz.R

class TaskAdapter (
    private val onEdit: (TaskEntity) -> Unit,
    private val onDelete: (TaskEntity) -> Unit,
    private val onChecked: (TaskEntity, Boolean) -> Unit,
    private val onMove: (Int, Int) -> Unit
) : ListAdapter<TaskEntity, TaskViewHolder>(TaskDiffCallback()) {
//
//    companion object DiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
//        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean =
//            oldItem.id == newItem.id
//
//        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean =
//            oldItem == newItem
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_todo_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, onEdit, onDelete, onChecked)
    }

    fun moveItem(from: Int, to: Int) {
        val currentList = currentList.toMutableList()
        val item = currentList.removeAt(from)
        currentList.add(to, item)
        submitList(currentList)
        onMove(from, to)
    }
}
class TaskDiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
    override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity) =
        oldItem.text == newItem.text && oldItem.isDone == newItem.isDone
}