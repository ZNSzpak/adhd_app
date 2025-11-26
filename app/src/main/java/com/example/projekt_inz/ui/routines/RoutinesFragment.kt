package com.example.projekt_inz.ui.routines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RoutinesFragment : Fragment() {
    private lateinit var viewModel: RoutinesViewModel

    private lateinit var buttonList: RecyclerView
    private lateinit var addButton: FloatingActionButton

    private lateinit var taskAdapter: RoutinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)

        viewModel = ViewModelProvider(this)[RoutinesViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupAddButton()
    }

    private fun findViews(view: View) {
        buttonList = view.findViewById(R.id.buttonList)
        addButton = view.findViewById(R.id.addButton)
    }

    private fun setupRecyclerView() {

    }

    private fun observeViewModel() {

    }

    private fun setupAddButton() {

    }
}