package com.example.projekt_inz.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.projekt_inz.R
import java.time.format.DateTimeFormatter


class EventAdapter(
    context: Context,
    private val events: List<Event>
) : ArrayAdapter<Event>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.event_cell, parent, false)

        val event = getItem(position)
        val eventCellTV = view.findViewById<TextView>(R.id.eventCellTV)

        if (event != null) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val eventTime = event.time.format(formatter)
            val eventTitle = "${event.name}  $eventTime"
            eventCellTV.text = eventTitle
        }

        return view
    }
}
