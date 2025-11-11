package com.example.projekt_inz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale


class HomeViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    private val _monthYearText = MutableLiveData<String>()
    val monthYearText: LiveData<String> = _monthYearText

    private val _daysInMonth = MutableLiveData<List<LocalDate?>>()
    val daysInMonth: LiveData<List<LocalDate?>> = _daysInMonth

    init {
        updateMonthView()
    }

    private fun updateMonthView() {
        val current = _selectedDate.value ?: LocalDate.now()
        _monthYearText.value = monthYearFromDate(current)
        _daysInMonth.value = daysInMonthArray(current)
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        updateMonthView()
    }

    fun previousMonth() {
        _selectedDate.value = _selectedDate.value?.minusMonths(1)
        updateMonthView()
    }

    fun nextMonth() {
        _selectedDate.value = _selectedDate.value?.plusMonths(1)
        updateMonthView()
    }


    fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        return date.format(formatter)
    }


    fun daysInMonthArray(date: LocalDate): List<LocalDate?> {
        val daysInMonthArray = mutableListOf<LocalDate?>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7

        for (i in 1..42) {
            val day = if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                null
            } else {
                LocalDate.of(date.year, date.month, i - dayOfWeek)
            }
            daysInMonthArray.add(day)
        }
        return daysInMonthArray
    }

//    fun daysInMonthArray(date: LocalDate): List<LocalDate?> {
//        val daysInMonthArray = mutableListOf<LocalDate?>()
//        val yearMonth = YearMonth.from(date)
//        val daysInMonth = yearMonth.lengthOfMonth()
//
//        val firstOfMonth = date.withDayOfMonth(1)
//        val dayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7  // Monday = 0
//
//        for (i in 1..42) {
//            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
//                daysInMonthArray.add(null)
//            } else {
//                daysInMonthArray.add(
//                    LocalDate.of(date.year, date.month, i - dayOfWeek)
//                )
//            }
//        }
//        return daysInMonthArray
//    }
//

}