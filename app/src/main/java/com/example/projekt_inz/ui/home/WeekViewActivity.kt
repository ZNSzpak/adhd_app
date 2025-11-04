package com.example.projekt_inz.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import java.time.LocalDate

class WeekViewActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var eventListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_view)
        initWidgets()
        setWeekView()
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
        eventListView = findViewById(R.id.event_list)
    }

    private fun setWeekView() {
        monthYearText.text = CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)
        val days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate)

//        val calendarAdapter = CalendarAdapter(days, this)
//        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
//        calendarRecyclerView.adapter = calendarAdapter

       // setEventAdapter()
    }

    fun previousWeekAction(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
        setWeekView()
    }

    override fun onItemClick(position: Int, dayText: String?) {
        // You may need to adapt CalendarAdapter to pass LocalDate instead of String
        // For now, let's assume CalendarUtils handles it
        Toast.makeText(this, "Clicked $dayText", Toast.LENGTH_SHORT).show()
    }

//    override fun onItemClick(position: Int, date: LocalDate?) {
//        if (date != null) {
//            CalendarUtils.selectedDate = date
//            setWeekView()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        setEventAdapter()
//    }

//    private fun setEventAdapter() {
//        val dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate)
//        val eventAdapter = EventAdapter(this, dailyEvents)
//        eventListView.adapter = eventAdapter
//    }

    fun newEventAction(view: View) {
        startActivity(Intent(this, EventEditActivity::class.java))
    }
}