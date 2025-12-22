package com.example.projekt_inz.ui.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import java.time.LocalDate

class CalendarAdapter(
    private val days: List<LocalDate?>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell_week, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15)  //month view
            layoutParams.height = (parent.height * 0.166666666).toInt()
        else  // week view
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]

        if (date == null) {
            holder.dayOfMonth.text = ""
            holder.parentView.setBackgroundResource(0)
            return
        }

        holder.dayOfMonth.text = date.dayOfMonth.toString()

        when {
            date.isEqual(LocalDate.now()) -> {
                holder.parentView.setBackgroundColor(Color.LTGRAY)
            }
            date.isEqual(CalendarUtils.selectedDate) -> {
                holder.parentView.setBackgroundColor(Color.YELLOW)
            }
            else -> {
                holder.dayOfMonth.setTextColor(Color.BLACK)
                holder.parentView.setBackgroundResource(0)
            }
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}
