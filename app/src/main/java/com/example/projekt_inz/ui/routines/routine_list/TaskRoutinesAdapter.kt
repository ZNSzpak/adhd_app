package com.example.projekt_inz.ui.routines.routine_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R

class TaskRoutinesAdapter (
    private val onEdit: (TaskEntityRoutines) -> Unit,
    private val onDelete: (TaskEntityRoutines) -> Unit,
    private val onChecked: (TaskEntityRoutines, Boolean) -> Unit
) : ListAdapter<TaskEntityRoutines, TaskRoutinesAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_todo_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        private val taskText: TextView = itemView.findViewById(R.id.taskText)
        private val editButton: ImageView = itemView.findViewById(R.id.editTask)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteTask)

        fun bind(task: TaskEntityRoutines) {
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = task.isDone

            taskText.text = task.text

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onChecked(task, isChecked)
            }

            editButton.setOnClickListener {
                onEdit(task)
            }

            deleteButton.setOnClickListener {
                onDelete(task)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<TaskEntityRoutines>() {
    override fun areItemsTheSame(oldItem: TaskEntityRoutines, newItem: TaskEntityRoutines): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskEntityRoutines, newItem: TaskEntityRoutines): Boolean {
        return oldItem.text == newItem.text &&
                oldItem.isDone == newItem.isDone &&
                oldItem.listId == newItem.listId
    }
}