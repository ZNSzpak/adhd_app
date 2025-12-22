package com.example.projekt_inz.ui.calendar.week_view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.CalendarAdapter
import com.example.projekt_inz.ui.calendar.CalendarUtils
import java.time.LocalDate

class WeekViewFragment : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var eventListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_week_view, container, false)

        initWidgets(view)

        val dateString = arguments?.getString(ARG_SELECTED_DATE)
        CalendarUtils.selectedDate = if (dateString != null) {
            LocalDate.parse(dateString)
        } else {
            LocalDate.now()
        }

        setWeekView()

        return view
    }

    private fun initWidgets(view: View) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        monthYearText = view.findViewById(R.id.monthYearTV)
        eventListView = view.findViewById(R.id.event_list)
    }

    private fun setWeekView() {
        monthYearText.text =
            CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)

        val days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate)

        val calendarAdapter = CalendarAdapter(days, this)
        calendarRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter

        setEventAdapter()
    }

    fun previousWeekAction(view: View) {
        CalendarUtils.selectedDate =
            CalendarUtils.selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View) {
        CalendarUtils.selectedDate =
            CalendarUtils.selectedDate.plusWeeks(1)
        setWeekView()
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            setWeekView()
        }
    }

    override fun onResume() {
        super.onResume()
        setEventAdapter()
    }

    private fun setEventAdapter() {
        val dailyEvents =
            Event.eventsForDate(CalendarUtils.selectedDate)

        val eventAdapter =
            EventAdapter(requireContext(), dailyEvents)

        eventListView.adapter = eventAdapter
    }

    fun newEventAction(view: View) {
        startActivity(
            Intent(requireContext(), EventEditActivity::class.java)
        )
    }

    companion object {
        private const val ARG_SELECTED_DATE = "selectedDate"

        fun newInstance(selectedDate: LocalDate?): WeekViewFragment {
            val fragment = WeekViewFragment()
            val args = Bundle()
            selectedDate?.let {
                args.putString(ARG_SELECTED_DATE, it.toString())
            }
            fragment.arguments = args
            return fragment
        }
    }
}
