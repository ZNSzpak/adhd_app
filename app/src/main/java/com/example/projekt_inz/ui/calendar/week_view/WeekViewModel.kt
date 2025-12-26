package com.example.projekt_inz.ui.calendar.week_view

import java.time.LocalDate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_inz.ui.calendar.EventEntity
import com.example.projekt_inz.ui.calendar.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.YearMonth
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class WeekViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val eventsForSelectedDay: StateFlow<List<EventEntity>> =
        selectedDate.flatMapLatest { date ->

            val startOfDay = date
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val endOfDay = date
                .atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            repository.getEventsInRange(startOfDay, endOfDay)

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    val daysInWeek: StateFlow<List<LocalDate>> =
        selectedDate.map { date ->
            val start = date.with(DayOfWeek.MONDAY)
            (0..6).map { start.plusDays(it.toLong()) }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val monthYearText: StateFlow<String> =
        selectedDate.map {
            it.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ""
        )

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun previousWeek() {
        _selectedDate.value = _selectedDate.value.minusWeeks(1)
    }

    fun nextWeek() {
        _selectedDate.value = _selectedDate.value.plusWeeks(1)
    }

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.insert(event)
        }
    }
}