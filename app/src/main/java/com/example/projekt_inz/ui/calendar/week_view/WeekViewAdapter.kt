package com.example.projekt_inz.ui.calendar.week_view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.EventEntity
import java.time.LocalDate

class WeekViewAdapter(
    private var days: List<LocalDate> = emptyList(),
    private var events: Map<LocalDate, List<EventEntity>> = emptyMap(),
    private var selectedDate: LocalDate = LocalDate.now(),
    private val onDayClick: (LocalDate) -> Unit
) : RecyclerView.Adapter<WeekViewAdapter.WeekDayViewHolder>() {

    fun submitDays(newDays: List<LocalDate>) {
        days = newDays
        notifyDataSetChanged()
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
        notifyDataSetChanged()
    }

    fun submitEvents(newEvents: Map<LocalDate, List<EventEntity>>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell_week, parent, false)
        return WeekDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekDayViewHolder, position: Int) {
        val date = days[position]
        holder.bind(date, selectedDate)
    }

    override fun getItemCount(): Int = days.size

    inner class WeekDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.cellDayText)
        private val parentView: View = itemView.findViewById(R.id.parentView)
        private val eventDot: View = itemView.findViewById(R.id.eventDot)


        fun bind(date: LocalDate, selectedDate: LocalDate) {
            dayText.text = date.dayOfMonth.toString()

            parentView.setBackgroundResource(0)
            dayText.setTextColor(Color.BLACK)

            when {
                date == LocalDate.now() ->
                    parentView.setBackgroundColor(Color.LTGRAY)

                date == selectedDate ->
                    parentView.setBackgroundColor(Color.parseColor("#ADD8E6"))
            }

            eventDot.visibility =
                if (events[date]?.isNotEmpty() == true)
                    View.VISIBLE
                else
                    View.GONE

            itemView.setOnClickListener { onDayClick(date) }
        }
    }
}
