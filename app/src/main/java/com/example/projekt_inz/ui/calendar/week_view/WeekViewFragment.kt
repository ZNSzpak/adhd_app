package com.example.projekt_inz.ui.calendar.week_view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.AddEventDialogFragment
import com.example.projekt_inz.ui.calendar.CalendarAdapter
import com.example.projekt_inz.ui.calendar.EventDatabase
import com.example.projekt_inz.ui.calendar.EventRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeekViewFragment : Fragment() {

    private lateinit var viewModel: WeekViewModel

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var addEventButton: Button

    private lateinit var weekAdapter: WeekViewAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_week_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = EventDatabase.getInstance(requireContext()).eventDao()
        val repository = EventRepository(dao)
        val factory = WeekViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[WeekViewModel::class.java]

        findViews(view)
        setupRecyclerView()
        setupButtons()
        observeViewModel()
    }

    private fun findViews(view: View) {
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        previousButton = view.findViewById(R.id.week_navigation_previous)
        nextButton = view.findViewById(R.id.week_navigation_next)
        addEventButton = view.findViewById(R.id.newEventButton)
    }

    private fun setupRecyclerView() {
        weekAdapter = WeekViewAdapter { selectedDate ->
            viewModel.selectDate(selectedDate)
        }

        calendarRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = weekAdapter
        }

        // You can later replace this with a RecyclerView
        eventAdapter = EventAdapter(emptyList())
        eventRecyclerView.adapter = eventAdapter
    }

    private fun setupButtons() {
        previousButton.setOnClickListener {
            viewModel.previousWeek()
        }

        nextButton.setOnClickListener {
            viewModel.nextWeek()
        }

        addEventButton.setOnClickListener {
            val selectedDateMillis = viewModel.selectedDate.value.toEpochDay() * 24*60*60*1000 // millis
            AddEventDialogFragment(selectedDateMillis) { newEvent ->
                viewModel.addEvent(newEvent)
            }.show(parentFragmentManager, "AddEventDialog")
        }
    }

    // ----------------------------------------------------
    // ViewModel observation
    // ----------------------------------------------------

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.monthYearText.collect { text ->
                        monthYearText.text = text
                    }
                }

                launch {
                    viewModel.daysInWeek.collect { days ->
                        weekAdapter.submitDays(days)
                    }
                }

                launch {
                    viewModel.eventsForSelectedDay.collect { events ->
                        eventAdapter.updateEvents(events)
                    }
                }
            }
        }
    }
}
