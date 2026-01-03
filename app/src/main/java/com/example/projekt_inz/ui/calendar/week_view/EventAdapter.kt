package com.example.projekt_inz.ui.calendar.week_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.EventEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventAdapter(
    private var events: List<EventEntity>,
    private val onEdit: (EventEntity) -> Unit,
    private val onDelete: (EventEntity) -> Unit
    ) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.eventName)
        val dateText: TextView = itemView.findViewById(R.id.eventDate)
        val timeText: TextView = itemView.findViewById(R.id.eventTime)
        val editBtn: ImageView = itemView.findViewById(R.id.editNB)
        val deleteBtn: ImageView = itemView.findViewById(R.id.deleteNB)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_cell, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.nameText.text = event.name
        val eventDate = LocalDate.ofEpochDay(event.dateEpochDay)
        holder.dateText.text = eventDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

        holder.editBtn.setOnClickListener { onEdit(event) }
        holder.deleteBtn.setOnClickListener { onDelete(event) }


        fun formatMinutes(minutes: Int): String = "%02d:%02d".format(minutes / 60, minutes % 60)
        holder.timeText.text = "${formatMinutes(event.startMinute)} - ${formatMinutes(event.endMinute)}"
    }
    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<EventEntity>) {
        events = newEvents
        notifyDataSetChanged()
    }
}