package com.example.projekt_inz.ui.calendar

import android.graphics.Color
import com.example.projekt_inz.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class CalendarViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView){

   // private var date: LocalDate? = null

    private val dayOfMonth: TextView =
        itemView.findViewById(R.id.cellDayText)

    private val parentView: View =
        itemView.findViewById(R.id.parentView)

    private val eventDot: View =
        itemView.findViewById(R.id.eventDot)

    fun bind(
        date: LocalDate?,
        events: List<EventEntity>?,
        isSelected: Boolean,
        onClick: (LocalDate) -> Unit
    ) {
        if (date == null) {
            itemView.visibility = View.INVISIBLE
            return
        }

        itemView.visibility = View.VISIBLE
        dayOfMonth.text = date.dayOfMonth.toString()


        if (isSelected) {
            parentView.setBackgroundColor(Color.parseColor("#ADD8E6"))
        }

        if (date == LocalDate.now()) {
            parentView.setBackgroundColor(Color.LTGRAY)
        }

        eventDot.visibility =
            if (!events.isNullOrEmpty()) View.VISIBLE else View.GONE

        itemView.setOnClickListener { onClick(date) }
    }

//    fun bind(date: LocalDate?, events: List<EventEntity>?, selectedDate: LocalDate) {
//        this.date = date
//
//        dayOfMonth.text = ""
//        dayOfMonth.setTextColor(Color.BLACK)
//        parentView.setBackgroundResource(0)
//
//        if (date == null) return
//
//        dayOfMonth.text = date.dayOfMonth.toString()
//
//        when {
//            date.isEqual(LocalDate.now()) -> parentView.setBackgroundColor(Color.LTGRAY)
//            date.isEqual(selectedDate) -> parentView.setBackgroundColor(Color.parseColor("#ADD8E6"))
//        }
//    }


}

