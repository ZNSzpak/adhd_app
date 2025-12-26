package com.example.projekt_inz.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarViewModel(
    private val repository: EventRepository
) : ViewModel() {

    // Selected date as StateFlow
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // Month-Year text as Flow
    val monthYearText: StateFlow<String> = _selectedDate
        .map { monthYearFromDate(it) }
        .stateIn(viewModelScope, SharingStarted.Lazily, monthYearFromDate(LocalDate.now()))

    // Days in month as Flow
    val daysInMonth: StateFlow<List<LocalDate?>> = _selectedDate
        .map { daysInMonthArray(it) }
        .stateIn(viewModelScope, SharingStarted.Lazily, daysInMonthArray(LocalDate.now()))

    // Events for the month as Flow
    val eventsForMonth: StateFlow<List<EventEntity>> = _selectedDate
        .map { YearMonth.from(it) }
        .flatMapLatest { ym ->
            val start = ym.atDay(1).toEpochDay()
            val end = ym.atEndOfMonth().toEpochDay()
            repository.getEventsInRange(start, end)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Events for selected day as Flow
    val eventsForSelectedDay: StateFlow<List<EventEntity>> = _selectedDate
        .flatMapLatest { date ->
            repository.getEventsForDay(date.toEpochDay())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // -----------------------------
    // Functions to update state
    // -----------------------------
    fun previousMonth() {
        _selectedDate.value = _selectedDate.value.minusMonths(1)
    }

    fun nextMonth() {
        _selectedDate.value = _selectedDate.value.plusMonths(1)
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        return date.format(formatter)
    }

    fun daysInMonthArray(date: LocalDate): List<LocalDate?> {
        val daysList = mutableListOf<LocalDate?>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7 // 0 = Monday

        for (i in 1..42) {
            val day = if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) null
            else LocalDate.of(date.year, date.month, i - dayOfWeek)
            daysList.add(day)
        }
        return daysList
    }

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.insert(event)
        }
    }
}
