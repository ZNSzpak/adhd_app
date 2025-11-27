package com.example.projekt_inz.ui.todolist

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    private val taskText: TextView = itemView.findViewById(R.id.taskText)
    private val editTask: ImageView = itemView.findViewById(R.id.editTask)
    private val deleteTask: ImageView = itemView.findViewById(R.id.deleteTask)

    fun bind(
        task: TaskEntity,
        onEdit: (TaskEntity) -> Unit,
        onDelete: (TaskEntity) -> Unit,
        onChecked: (TaskEntity, Boolean) -> Unit
    ) {
        taskText.text = task.text
        checkbox.isChecked = task.isDone

        // actions
        editTask.setOnClickListener { onEdit(task) }
        deleteTask.setOnClickListener { onDelete(task) }
        checkbox.setOnCheckedChangeListener { _, checked ->
            onChecked(task, checked)
        }
    }
}