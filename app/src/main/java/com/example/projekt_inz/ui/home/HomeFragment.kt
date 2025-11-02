package com.example.projekt_inz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class HomeFragment : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)

        view.findViewById<Button>(R.id.month_navigation_previous).setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        view.findViewById<Button>(R.id.month_navigation_next).setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }

        setMonthView()
        return view
    }

    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, this)


        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter
    }



    // Format month/year
    private fun monthYearFromDate(date: LocalDate): String {
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val year = date.year
        return "$month $year"
    }

    // Return a list of strings for days of month, empty strings for padding
    private fun daysInMonthArray(date: LocalDate): List<String> {
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        // Shift days so Monday = 0, Tuesday = 1, ..., Sunday = 6
        val firstDayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7

        val daysArray = mutableListOf<String>()

        // Add empty days before 1st
        for (i in 0 until firstDayOfWeek) {
            daysArray.add("")
        }

        // Add actual days
        for (day in 1..daysInMonth) {
            daysArray.add(day.toString())
        }

        return daysArray
    }

    override fun onItemClick(position: Int, dayText: String?) {
        Toast.makeText(requireContext(), "Clicked day: $dayText", Toast.LENGTH_SHORT).show()
    }
}
