package com.example.projekt_inz.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.home.CalendarUtils.selectedDate
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale


class EventEditActivity : AppCompatActivity() {

    private lateinit var eventNameET: EditText
    private lateinit var eventDateTV: TextView
    private lateinit var eventTimeTV: TextView
    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedTime: LocalTime = LocalTime.now()

    private var hour: Int = 0
    private var minute: Int = 0

    private var time: LocalTime = LocalTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)
        //toolbar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // shows ←
            title = ""
        }

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());

        timeButton = findViewById(R.id.timeButton);

        initWidgets()

        dateButton.text = formatDate(selectedDate)
        timeButton.text = formatTime(selectedTime)
        initDatePicker();

      //  dateButton.text = getTodayDate()

    }

    private fun formatDate(date: LocalDate): String {
        val month = date.month.toString().substring(0, 3)
        return "${date.dayOfMonth} ${month.uppercase(Locale.getDefault())} ${date.year}"
    }

    private fun formatTime(time: LocalTime): String {
        return String.format(Locale.getDefault(), "%02d:%02d", time.hour, time.minute)
    }

    private fun getTodayDate(): String {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month = month + 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return  " " + day + " " + getMonthFormat(month) +" " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        if (month == 12) return "DEC"

        //default should never happen
        return "JAN"
    }


//    private fun initDatePicker() {
//        val dateSetListener =
//            OnDateSetListener { datePicker, year, month, day ->
//                var month = month
//                month += 1
//                val date = makeDateString(day, month, year)
//                dateButton.text = date
//            }
//
//        val cal = Calendar.getInstance()
//        val year = cal[Calendar.YEAR]
//        val month = cal[Calendar.MONTH]
//        val day = cal[Calendar.DAY_OF_MONTH]
//
//       val style: Int = AlertDialog.BUTTON_POSITIVE
//
//        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
//
//
//    }

    private fun initDatePicker() {
        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val day = today.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            selectedDate = LocalDate.of(y, m + 1, d)
            dateButton.text = formatDate(selectedDate)
        }

        dateButton.setOnClickListener {
            DatePickerDialog(this, dateSetListener, year, month, day).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initWidgets() {
        eventNameET = findViewById(R.id.eventNameET)
        dateButton = findViewById(R.id.datePickerButton)
        timeButton = findViewById(R.id.timeButton)
    }

//    fun saveEventAction(view: View) {
//        val eventName = eventNameET.text.toString().trim()
//        val eventTime = "todo"
//        val eventDate = "todo"
//        if (eventName.isNotEmpty()) {
//            val newEvent = Event(eventName, eventDate, eventTime)
//            Event.eventsList.add(newEvent)
//        }
//        finish()
//    }

    fun saveEventAction(view: View) {
        val eventName = eventNameET.text.toString().trim()

        if (eventName.isEmpty()) {
            eventNameET.error = "Wpisz nazwę wydarzenia"
            return
        }

        val newEvent = Event(eventName, selectedDate, selectedTime)
        Event.eventsList.add(newEvent)

        finish()
    }

    fun openDatePicker(view: View) {
        datePickerDialog?.show()
    }
//    fun popTimePicker(view: View?) {
//        val onTimeSetListener =
//            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//                 hour = selectedHour
//                 minute = selectedMinute
//                timeButton.text =
//                    java.lang.String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
//            }
//
//        val style: Int = AlertDialog.BUTTON_POSITIVE
//
//        val timePickerDialog =
//            TimePickerDialog(this,  style, onTimeSetListener, hour, minute, true)
//
//        timePickerDialog.setTitle("Select Time")
//        timePickerDialog.show()
//    }

    fun popTimePicker(view: View) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            selectedTime = LocalTime.of(hour, minute)
            timeButton.text = formatTime(selectedTime)
        }

        TimePickerDialog(
            this,
            AlertDialog.THEME_HOLO_LIGHT,
            timeSetListener,
            selectedTime.hour,
            selectedTime.minute,
            true
        ).apply {
            setTitle("Wybierz godzinę")
            show()
        }
    }
}