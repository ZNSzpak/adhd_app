package com.example.projekt_inz.ui.calendar.week_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.EventEntity

class EventAdapter(private var events: List<EventEntity>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.eventName)
        val timeText: TextView = itemView.findViewById(R.id.eventTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_cell, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.nameText.text = event.name
        holder.timeText.text = "${event.startMinute/60}:${event.startMinute%60} - ${event.endMinute/60}:${event.endMinute%60}"
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<EventEntity>) {
        events = newEvents
        notifyDataSetChanged()
    }
}