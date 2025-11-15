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

    private lateinit var grid: GridLayout
    private val startHour = 8
    private val endHour = 18 // exclusive, so last hour shown is 17

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)
        grid = view.findViewById(R.id.timetableGrid)
        setupGrid()
        return view
    }

    private fun setupGrid() {
        val totalRows = (endHour - startHour) + 1
        grid.rowCount = totalRows + 1  // +1 for header

        // ---- HEADER ----
        val headers = listOf("Time", "Mon", "Tue", "Wed", "Thu", "Fri")

        for (col in 0 until 6) {
            val header = TextView(context).apply {
                text = headers[col]
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setBackgroundResource(R.drawable.cell_border)

            }
            grid.addView(header)
        }

        // ---- HOURS ----
        for (i in 0 until totalRows) {
            val hour = startHour + i

            for (col in 0 until 6) {
                val cell = TextView(context).apply {
                    setPadding(8, 24, 8, 24)
                    gravity = Gravity.CENTER
                    setBackgroundResource(R.drawable.cell_border)

                    if (col == 0)
                        text = String.format("%02d:00", hour)
                }
                grid.addView(cell)
            }
        }
    }
}