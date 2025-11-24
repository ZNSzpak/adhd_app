package com.example.projekt_inz.ui.todolist

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    val taskText: TextView = itemView.findViewById(R.id.taskText)
    val editTask: ImageView = itemView.findViewById(R.id.editTask)
    val deleteTask: ImageView = itemView.findViewById(R.id.deleteTask)
}