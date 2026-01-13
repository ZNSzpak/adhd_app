package com.example.projekt_inz.ui.calendar.week_view

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.calendar.AddEventDialogFragment
import com.example.projekt_inz.ui.calendar.CalendarViewModel
import com.example.projekt_inz.ui.calendar.CalendarViewModelFactory
import com.example.projekt_inz.ui.calendar.EditEventDialogFragment
import com.example.projekt_inz.ui.calendar.EventDatabase
import com.example.projekt_inz.ui.calendar.EventRepository
import com.example.projekt_inz.ui.calendar.notifications.AlarmScheduler
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeekViewFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel

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

        val dateString = arguments?.getString("selectedDate")
        val selectedDate = dateString?.let { LocalDate.parse(it) } ?: LocalDate.now()

        val dao = EventDatabase.getInstance(requireContext()).eventDao()
        val repository = EventRepository(dao)
        val factory = CalendarViewModelFactory(repository)

        viewModel = ViewModelProvider(requireActivity(), factory)[CalendarViewModel::class.java]

        viewModel.selectDate(selectedDate)

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
        weekAdapter = WeekViewAdapter(
            days = emptyList(),
            selectedDate = viewModel.selectedDate.value
        ) { clickedDate ->
            viewModel.selectDate(clickedDate)  // update ViewModel
        }

        calendarRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = weekAdapter
        }

        eventAdapter = EventAdapter(
            events = emptyList(),
            onEdit = { event ->
                EditEventDialogFragment(event) { updated ->
                    viewModel.editEvent(event, updated, requireContext().applicationContext)

                }.show(parentFragmentManager, "EditEventDialog")
            },
            onDelete = { event ->
                viewModel.deleteEvent(event, requireContext().applicationContext)
            }
        )
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            val selectedDate = viewModel.selectedDate.value

            AddEventDialogFragment(selectedDate.toEpochDay()) { newEvent ->
                viewModel.addEvent(newEvent, requireContext().applicationContext)

            }.show(parentFragmentManager, "AddEventDialog")
        }
    }

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
                        weekAdapter.setSelectedDate(viewModel.selectedDate.value)
                    }
                }

                launch {
                    viewModel.eventsForSelectedDay.collect { events ->
                        Log.d("WeekView", "Events loaded: ${events.size}")
                        eventAdapter.updateEvents(events)
                    }
                }

                launch {
                    viewModel.eventsForWeek.collect { events ->
                        weekAdapter.submitEvents(events)
                    }
                }

                launch {
                    viewModel.selectedDate.collect { date ->
                        weekAdapter.setSelectedDate(date)  // highlight new date
                    }
                }
            }
        }
    }
}
