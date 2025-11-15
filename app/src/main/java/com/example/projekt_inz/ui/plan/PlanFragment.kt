package com.example.projekt_inz.ui.plan

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projekt_inz.R

class PlanFragment : Fragment() {

    private lateinit var timetableGrid: GridLayout
    private val startHour = 8
    private val endHour = 18 // exclusive, so last hour shown is 17



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)
        timetableGrid = view.findViewById(R.id.timetableGrid)




            addEvent(dayOfWeek = 3, startHour = 9, endHour = 12)
            addEvent(dayOfWeek = 5, startHour = 10, endHour = 13)


        return view
    }

    private fun addEvent(
        dayOfWeek: Int,
        startHour: Int,
        endHour: Int
    ) {
        val inflater = LayoutInflater.from(context)
        val eventView = inflater.inflate(R.layout.plan_cell, timetableGrid, false)

        val startRow = (startHour - this.startHour) + 1
        val durationRows = endHour - startHour

        val params = GridLayout.LayoutParams().apply {
            rowSpec = GridLayout.spec(startRow, durationRows)
            columnSpec = GridLayout.spec(dayOfWeek)
            width = 0
            height = 0
            setGravity(Gravity.FILL)
        }

        eventView.layoutParams = params
        timetableGrid.addView(eventView)
    }
}
