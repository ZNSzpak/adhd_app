package com.example.projekt_inz.ui.home
import com.example.projekt_inz.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class CalendarViewHolder(itemView: View,
                         private val onItemListener: CalendarAdapter.OnItemListener,
                         private val days: List<LocalDate?>) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
   // val parentView: View = itemView.findViewById(R.id.parentView)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            onItemListener.onItemClick(position, days[position])
        }
    }
}