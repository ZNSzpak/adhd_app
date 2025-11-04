package com.example.projekt_inz.ui.home

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

        viewModel.daysInMonth.observe(viewLifecycleOwner) { days ->
            val adapter = CalendarAdapter(days, this)
            calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
            calendarRecyclerView.adapter = adapter
        }

        viewModel.monthYearText.observe(viewLifecycleOwner) { text ->
            monthYearText.text = text
        }


        view.findViewById<Button>(R.id.month_navigation_previous).setOnClickListener {
            viewModel.previousMonth()
        }

        view.findViewById<Button>(R.id.month_navigation_next).setOnClickListener {
            viewModel.nextMonth()
        }

// kod związany z weekly events
//        view.findViewById<Button>(R.id.weekly_button).setOnClickListener {
//            val intent = Intent(requireContext(), WeekViewActivity::class.java)
//            startActivity(intent)
//        }

        return view
    }

    override fun onItemClick(position: Int, dayText: String?) {
        Toast.makeText(requireContext(), "Clicked day: $dayText", Toast.LENGTH_SHORT).show()
    }
}
