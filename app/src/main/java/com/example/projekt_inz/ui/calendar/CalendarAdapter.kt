package com.example.projekt_inz.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import java.time.LocalDate

class CalendarAdapter(
    private var days: List<LocalDate?> = emptyList(),
    private var eventsMap: Map<LocalDate, List<EventEntity>> = emptyMap(),
    private var selectedDate: LocalDate = LocalDate.now(),
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarViewHolder>() {

    fun selectDate(date: LocalDate) {
        selectedDate = date
        notifyDataSetChanged()
    }

    fun submitDays(newDays: List<LocalDate?>) {
        days = newDays
        notifyDataSetChanged()
    }

    fun submitEvents(newEvents: Map<LocalDate, List<EventEntity>>) {
        eventsMap = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell_month, parent, false)
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        val events = date?.let { eventsMap[it] }
        holder.bind(date, events, selectedDate)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}
