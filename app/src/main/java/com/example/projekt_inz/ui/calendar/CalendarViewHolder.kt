package com.example.projekt_inz.ui.calendar

import android.graphics.Color
import com.example.projekt_inz.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class CalendarViewHolder(itemView: View,
                         private val onItemListener: CalendarAdapter.OnItemListener
    ) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var date: LocalDate? = null

    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    val parentView: View = itemView.findViewById(R.id.parentView)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(date: LocalDate?, events: List<EventEntity>?, selectedDate: LocalDate) {
        this.date = date

        dayOfMonth.text = ""
        dayOfMonth.setTextColor(Color.BLACK)
        parentView.setBackgroundResource(0)

        if (date == null) return

        dayOfMonth.text = date.dayOfMonth.toString()

        when {
            date.isEqual(LocalDate.now()) -> parentView.setBackgroundColor(Color.LTGRAY)
            date.isEqual(selectedDate) -> parentView.setBackgroundColor(Color.parseColor("#ADD8E6"))
        }
    }

    override fun onClick(view: View) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemListener.onItemClick(adapterPosition, date)
        }
    }
}

