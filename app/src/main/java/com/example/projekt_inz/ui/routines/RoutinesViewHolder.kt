package com.example.projekt_inz.ui.routines

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R

class RoutinesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val button: Button = view.findViewById(R.id.listButton)
    val editButton: ImageView = itemView.findViewById(R.id.editB)
    val deleteButton: ImageView = itemView.findViewById(R.id.deleteB)
}