package com.example.projekt_inz.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.week_view.WeekViewFragment
import java.time.LocalDate

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var viewModel: CalendarViewModel

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var weeklyViewButton: Button

    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)

        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        setupButtons()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupButtons() {
        previousButton.setOnClickListener { viewModel.previousMonth() }
        nextButton.setOnClickListener { viewModel.nextMonth() }
        weeklyViewButton.setOnClickListener {weeklyAction() }
    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarAdapter(emptyList(), this)
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter
    }
    private fun setMonthView() {
        monthYearText.text = viewModel.monthYearFromDate(CalendarUtils.selectedDate)
        val daysInMonth = viewModel.daysInMonthArray(CalendarUtils.selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun findViews(view: View) {
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        previousButton = view.findViewById(R.id.month_navigation_previous)
        nextButton = view.findViewById(R.id.month_navigation_next)
        weeklyViewButton = view.findViewById(R.id.weekly_button)
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

    private fun weeklyAction() {
//        val intent = Intent(context, WeekViewFragment::class.java)
//        intent.putExtra("selectedDate", CalendarUtils.selectedDate.toString())
//        startActivity(intent)
        val selectedDate = viewModel._selectedDate.value ?: LocalDate.now()

        val bundle = Bundle().apply {
            putString("selectedDate", selectedDate.toString())
        }

        findNavController().navigate(
            R.id.action_nav_home_to_WeekViewFragment,
            bundle
        )
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            setMonthView()
        }
    }
}
