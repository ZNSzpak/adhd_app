package com.example.projekt_inz.ui.calendar

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AddEventDialogFragment(
    private val initialDateMillis: Long,
    private val onEventAdded: (EventEntity) -> Unit
) : DialogFragment() {

    private lateinit var editEventName: EditText
    private lateinit var datePickerButton: Button
    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
    private lateinit var addButton: Button
    private lateinit var closeButton: ImageView

    private lateinit var selectedDate: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
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

        editEventName = view.findViewById(R.id.editEventName)
        datePickerButton = view.findViewById(R.id.datePickerButtonEvent)
        startTimePicker = view.findViewById(R.id.startTimePickerEvent)
        endTimePicker = view.findViewById(R.id.endTimePickerEvent)
        addButton = view.findViewById(R.id.btnAddBEvent)
        closeButton = view.findViewById(R.id.closeButtonAddEvent)

        startTimePicker.setIs24HourView(true)
        endTimePicker.setIs24HourView(true)

        selectedDate = Instant.ofEpochMilli(initialDateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        updateDateButton()

        datePickerButton.setOnClickListener {
            openDatePicker()
        }

        closeButton.setOnClickListener { dismiss() }

        addButton.setOnClickListener { saveEvent() }
    }

    private fun updateDateButton() {
        datePickerButton.text = selectedDate.toString()
    }

    private fun openDatePicker() {
        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
                updateDateButton()
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        dialog.show()
    }

    private fun saveEvent() {
        val name = editEventName.text.toString().trim()
        if (name.isEmpty()) {
            editEventName.error = "Enter event name"
            return
        }

        val startMinutes = startTimePicker.hour * 60 + startTimePicker.minute
        val endMinutes = endTimePicker.hour * 60 + endTimePicker.minute

        if (endMinutes <= startMinutes) {
            Toast.makeText(requireContext(), "End time must be after start time", Toast.LENGTH_SHORT).show()
            return
        }

        val event = EventEntity(
            name = name,
            dateEpochDay = selectedDate.toEpochDay(),
            startMinute = startMinutes,
            endMinute = endMinutes
        )

        onEventAdded(event)
        dismiss()
    }

//    private fun saveEvent() {
//        val name = editEventName.text.toString().trim()
//        if (name.isEmpty()) {
//            editEventName.error = "Enter event name"
//            return
//        }
//
//        val startMinutes = startTimePicker.hour * 60 + startTimePicker.minute
//        val endMinutes = endTimePicker.hour * 60 + endTimePicker.minute
//
//        if (endMinutes <= startMinutes) {
//            Toast.makeText(requireContext(), "End time must be after start time", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val event = EventEntity(
//            name = name,
//            dateEpochDay = selectedDate.toEpochDay(),
//            startMinute = startMinutes,
//            endMinute = endMinutes
//        )
//
//        onEventAdded(event)
//        dismiss()
//    }
}