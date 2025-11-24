package com.example.projekt_inz.ui.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R

class TaskAdapter (
    private val tasks: MutableList<Task>,
    private val onEdit: (Task, Int) -> Unit,
    private val onDelete: (Task, Int) -> Unit,
    private val onChecked: (Task, Int, Boolean) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_todo_item, parent, false)   // your XML filename
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.taskText.text = task.text
        holder.checkbox.isChecked = task.isDone

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
            onChecked(task, position, isChecked)
        }

        holder.editTask.setOnClickListener {
            onEdit(task, position)
        }

        holder.deleteTask.setOnClickListener {
            onDelete(task, position)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }

    fun removeTask(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateTask(position: Int, newText: Task) {
        tasks[position].text = newText.toString()
        notifyItemChanged(position)
    }
}