package com.example.projekt_inz.ui.calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.R
import java.time.LocalDate

class EditEventDialogFragment(
    private val event: EventEntity,
    private val onEventUpdated: (EventEntity) -> Unit
) : DialogFragment() {

    private lateinit var editEventName: EditText
    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
    private lateinit var saveButton: Button
    private lateinit var closeButton: ImageView
    private lateinit var dateButton: Button

    private lateinit var selectedDate: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDate = LocalDate.ofEpochDay(event.dateEpochDay)

        editEventName = view.findViewById(R.id.editEventName)
        startTimePicker = view.findViewById(R.id.startTimePickerEvent)
        endTimePicker = view.findViewById(R.id.endTimePickerEvent)
        saveButton = view.findViewById(R.id.btnAddBEvent)
        closeButton = view.findViewById(R.id.closeButtonAddEvent)
        dateButton = view.findViewById(R.id.datePickerButtonEvent)

        startTimePicker.setIs24HourView(true)
        endTimePicker.setIs24HourView(true)

        // Pre-fill data
        editEventName.setText(event.name)
        startTimePicker.hour = event.startMinute / 60
        startTimePicker.minute = event.startMinute % 60
        endTimePicker.hour = event.endMinute / 60
        endTimePicker.minute = event.endMinute % 60

        saveButton.text = "Save"
        updateDateButton()

        dateButton.setOnClickListener { openDatePicker() }
        closeButton.setOnClickListener { dismiss() }
        saveButton.setOnClickListener { saveChanges() }
    }

    private fun openDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
                updateDateButton()
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        ).show()
    }
    private fun updateDateButton() {
        dateButton.text = selectedDate.toString()
    }

    private fun saveChanges() {
        val name = editEventName.text.toString().trim()
        if (name.isEmpty()) {
            editEventName.error = "Enter event name"
            return
        }

        val start = startTimePicker.hour * 60 + startTimePicker.minute
        val end = endTimePicker.hour * 60 + endTimePicker.minute

        if (end <= start) return

        onEventUpdated(
            event.copy(
                name = name,
                dateEpochDay = selectedDate.toEpochDay(),
                startMinute = start,
                endMinute = end
            )
        )
        dismiss()
    }
}