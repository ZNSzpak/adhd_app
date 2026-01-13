package com.example.projekt_inz.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var weeklyViewButton: Button
    private lateinit var addButton: FloatingActionButton

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

        val dao = EventDatabase.getInstance(requireContext()).eventDao()
        val repository = EventRepository(dao)
        val factory = CalendarViewModelFactory(repository)

        viewModel = ViewModelProvider(requireActivity(), factory)[CalendarViewModel::class.java]

        setupRecyclerView()
        setupButtons()
        observeViewModel()
    }

    private fun setupButtons() {
        previousButton.setOnClickListener { viewModel.previousMonth() }
        nextButton.setOnClickListener { viewModel.nextMonth() }
        weeklyViewButton.setOnClickListener {weeklyAction() }

        addButton.setOnClickListener {
            val selectedDate = viewModel.selectedDate.value

            AddEventDialogFragment(selectedDate.toEpochDay()) { newEvent ->
                viewModel.addEvent(newEvent, requireContext())
            }.show(parentFragmentManager, "AddEventDialog")
        }

    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarAdapter { date ->
            viewModel.selectDate(date)
        }

        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun findViews(view: View) {
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        previousButton = view.findViewById(R.id.month_navigation_previous)
        nextButton = view.findViewById(R.id.month_navigation_next)
        weeklyViewButton = view.findViewById(R.id.weekly_button)
        addButton = view.findViewById(R.id.addEventInMonthButton)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.monthYearText.collect {
                        monthYearText.text = it
                    }
                }

                launch {
                    combine(
                        viewModel.daysInMonth,
                        viewModel.eventsForMonth,
                        viewModel.selectedDate
                    ) { days, events, selected ->
                        Triple(days, events, selected)
                    }.collect { (days, events, selected) ->
                        calendarAdapter.submit(
                            days = days,
                            eventsMap = events.groupBy {
                                LocalDate.ofEpochDay(it.dateEpochDay)
                            },
                            selectedDate = selected
                        )
                    }
                }
            }
        }
    }
    private fun weeklyAction() {
        val bundle = Bundle().apply {
            putString("selectedDate", viewModel.selectedDate.value.toString())
        }

        findNavController().navigate(
            R.id.action_nav_home_to_WeekViewFragment,
            bundle
        )
    }

}
