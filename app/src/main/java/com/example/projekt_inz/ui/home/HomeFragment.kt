package com.example.projekt_inz.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


class HomeFragment : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        view.findViewById<Button>(R.id.month_navigation_previous).setOnClickListener {
            viewModel.previousMonth()
        }

        view.findViewById<Button>(R.id.month_navigation_next).setOnClickListener {
            viewModel.nextMonth()
        }

 
//weekly_button

        val weeklyViewButton = view.findViewById<Button>(R.id.weekly_button)

        weeklyViewButton.setOnClickListener {
            weeklyAction()
        }

        observeViewModel()
        return view
    }

    private fun observeViewModel() {
        viewModel.monthYearText.observe(viewLifecycleOwner) { text ->
            monthYearText.text = text
        }

        viewModel.daysInMonth.observe(viewLifecycleOwner) { days ->
            val calendarAdapter = CalendarAdapter(days, this)
            calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
            calendarRecyclerView.adapter = calendarAdapter
        }
    }


    private fun setMonthView() {
        monthYearText.text = viewModel.monthYearFromDate(CalendarUtils.selectedDate)
        val daysInMonth = viewModel.daysInMonthArray(CalendarUtils.selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter
    }
    
    //kod związany z weekly events
    private fun weeklyAction() {
        // Reset to the first week of the current month
//        val firstDayOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1)
//        CalendarUtils.selectedDate = if (firstDayOfMonth.dayOfWeek == DayOfWeek.MONDAY) {
//            firstDayOfMonth
//        } else {
//            firstDayOfMonth.plusDays((8 - firstDayOfMonth.dayOfWeek.value).toLong())
//        }

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.with(
            java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)
        )

        startActivity(Intent(requireContext(), WeekViewActivity::class.java))
    }

//    override fun onItemClick(position: Int, date: LocalDate?) {
//        if (date != null) {
//            viewModel.selectDate(date)
//            Toast.makeText(requireContext(), "Selected ${viewModel.monthYearFromDate(date)}", Toast.LENGTH_SHORT).show()
//        }
//    }


    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            setMonthView();
        }

    }


}
