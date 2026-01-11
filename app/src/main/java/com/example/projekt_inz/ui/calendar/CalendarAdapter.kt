package com.example.projekt_inz.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import java.time.LocalDate

class CalendarAdapter(
    private val onDateClick: (LocalDate) -> Unit
) : RecyclerView.Adapter<CalendarViewHolder>() {

    private var days: List<LocalDate?> = emptyList()
    private var eventsMap: Map<LocalDate, List<EventEntity>> = emptyMap()
    private var selectedDate: LocalDate? = null

    fun submit(
        days: List<LocalDate?>,
        eventsMap: Map<LocalDate, List<EventEntity>>,
        selectedDate: LocalDate
    ) {
        this.days = days
        this.eventsMap = eventsMap
        this.selectedDate = selectedDate
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell_month, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        holder.bind(
            date = date,
            events = date?.let { eventsMap[it] },
            isSelected = date == selectedDate,
            onClick = onDateClick
        )
    }

    override fun getItemCount() = days.size
}
