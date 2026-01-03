package com.example.projekt_inz.ui.calendar

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var viewModel: CalendarViewModel

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var weeklyViewButton: Button
    private lateinit var addButton: FloatingActionButton

    private lateinit var calendarAdapter: CalendarAdapter

    private var selectedDate: LocalDate = LocalDate.now()

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
                viewModel.addEvent(newEvent)
            }.show(parentFragmentManager, "AddEventDialog")
        }

    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarAdapter(emptyList(), emptyMap(),LocalDate.now(), this)
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

               launch{ viewModel.selectedDate.collect { date ->
                    calendarAdapter.selectDate(date)
                }}

                // Month-Year label
                launch {
                    viewModel.monthYearText.collect { text ->
                        monthYearText.text = text
                    }
                }

                // Days in month
                launch {
                    viewModel.daysInMonth.collect { days ->
                        calendarAdapter.submitDays(days)
                    }
                }

                // Events for the month
                launch {
                    viewModel.eventsForMonth.collect { events ->
                        val eventsMap = events.groupBy { LocalDate.ofEpochDay(it.dateEpochDay) }
                        calendarAdapter.submitEvents(eventsMap)
                    }
                }
            }
        }
    }

    private fun weeklyAction() {
        val selectedDate = viewModel.selectedDate.value ?: LocalDate.now()

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
            viewModel.selectDate(date)
           // calendarAdapter.selectDate(date)
        }
    }

}
