package com.example.projekt_inz.ui.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_inz.ui.calendar.notifications.AlarmScheduler
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
import java.time.DayOfWeek

class CalendarViewModel(
    private val repository: EventRepository
) : ViewModel() {

    // Selected date as StateFlow
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Events for selected day as Flow
    val eventsForSelectedDay: StateFlow<List<EventEntity>> = _selectedDate
        .flatMapLatest { date ->
            repository.getEventsForDay(date.toEpochDay())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val daysWithEvents: StateFlow<Set<LocalDate>> =
        eventsForMonth
            .map { events ->
                events.map { LocalDate.ofEpochDay(it.dateEpochDay) }.toSet()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )

    val eventsForWeek: StateFlow<Map<LocalDate, List<EventEntity>>> =
        selectedDate
            .map { date ->
                val start = date.with(DayOfWeek.MONDAY).toEpochDay()
                val end = date.with(DayOfWeek.SUNDAY).toEpochDay()
                start to end
            }
            .flatMapLatest { (start, end) ->
                repository.getEventsInRange(start, end)
            }
            .map { events ->
                events.groupBy { LocalDate.ofEpochDay(it.dateEpochDay) }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyMap()
            )


    val daysInWeek: StateFlow<List<LocalDate>> = _selectedDate
        .map { date ->
            val start = date.with(DayOfWeek.MONDAY)
            (0..6).map { start.plusDays(it.toLong()) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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
    fun previousWeek() { _selectedDate.value = _selectedDate.value.minusWeeks(1) }
    fun nextWeek() { _selectedDate.value = _selectedDate.value.plusWeeks(1) }

    companion object {
        val CUSTOM_MONTHS = listOf(
            "Styczeń",
            "Luty",
            "Marzec",
            "Kwiecień",
            "Maj",
            "Czerwiec",
            "Lipiec",
            "Sierpień",
            "Wrzesień",
            "Październik",
            "Listopad",
            "Grudzień"
        )
    }

    private fun monthYearFromDate(date: LocalDate): String {
//        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pl", "PL"))
//        return date.format(formatter)
        val index = date.monthValue - 1
        val monthName = CUSTOM_MONTHS.getOrElse(index) {
            date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }
        return "$monthName ${date.year}"
    }

    private fun daysInMonthArray(date: LocalDate): List<LocalDate?> {
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

    fun addEvent(event: EventEntity, context: Context) {
        viewModelScope.launch {

            val eventIdLong: Long = repository.insert(event)
            val eventId: Int = eventIdLong.toInt()

            val savedEvent = event.copy(id = eventId)

            val reminderTimeMillis = savedEvent.startTimeMillis() - 30 * 60 * 1000

            if (reminderTimeMillis > System.currentTimeMillis()) {
                AlarmScheduler.scheduleEventReminder(
                    context = context,
                    triggerAtMillis = reminderTimeMillis,
                    eventId = savedEvent.id,
                    eventName = savedEvent.name,
                    eventTime = savedEvent.formatTimeRange()
                )
            }
        }

    }

    fun editEvent(oldEvent: EventEntity, updated: EventEntity, context: Context) {
        viewModelScope.launch {
            // Cancel old alarm
            AlarmScheduler.cancelEventReminder(context, oldEvent.id)

            // Save updated event (schedules new alarm)
            addEvent(updated, context)
        }
    }

    fun deleteEvent(event: EventEntity, context: Context) {
        viewModelScope.launch {
            repository.delete(event)
            AlarmScheduler.cancelEventReminder(context, event.id)
        }
    }
}
