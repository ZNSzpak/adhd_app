package com.example.projekt_inz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeViewModel : ViewModel() {

    private var selectedDate: LocalDate = LocalDate.now()

    private val _daysInMonth = MutableLiveData<List<String>>()
    val daysInMonth: LiveData<List<String>> = _daysInMonth

    private val _monthYearText = MutableLiveData<String>()
    val monthYearText: LiveData<String> = _monthYearText

    init {
        updateMonthView()
    }

    fun previousMonth() {
        selectedDate = selectedDate.minusMonths(1)
        updateMonthView()
    }

    fun nextMonth() {
        selectedDate = selectedDate.plusMonths(1)
        updateMonthView()
    }

    private fun updateMonthView() {
        _monthYearText.value = monthYearFromDate(selectedDate)
        _daysInMonth.value = daysInMonthArray(selectedDate)
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        return date.format(formatter)
    }

    private fun daysInMonthArray(date: LocalDate): List<String> {
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val firstDayOfWeekIndex = (firstOfMonth.dayOfWeek.value + 6) % 7

        return buildList {
            repeat(firstDayOfWeekIndex) { add("") }
            for (day in 1..daysInMonth) add(day.toString())
        }
    }
}