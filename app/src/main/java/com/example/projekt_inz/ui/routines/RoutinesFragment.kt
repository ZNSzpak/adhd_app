package com.example.projekt_inz.ui.routines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.routines.routine_list.RoutineTaskDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RoutinesFragment : Fragment() {
    private lateinit var viewModel: RoutinesViewModel

    private lateinit var buttonList: RecyclerView
    private lateinit var addButton: FloatingActionButton

    private lateinit var adapter: RoutinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = RoutineTaskDatabase.getDatabase(requireContext())
        val routinesDao = db.routinesDao()
        val repository = RoutinesRepository(routinesDao)
        val factory = RoutinesViewModelFactory(repository)

        findViews(view)

        viewModel = ViewModelProvider(this, factory)[RoutinesViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupAddButton()

    }

    private fun findViews(view: View) {
        buttonList = view.findViewById(R.id.buttonList)
        addButton = view.findViewById(R.id.addButton)
    }

    private fun setupRecyclerView() {
        adapter = RoutinesAdapter(
            onClick = { item ->
                openTodoList(item)
            },
            onEditClick = { item ->
                EditButtonDialogFragment(item.title) { newName ->
                    viewModel.updateList(item, newName)
                }.show(parentFragmentManager, "EditListDialog")
            },
            onDeleteClick = { item ->
                viewModel.deleteList(item)
            }
        )

        buttonList.layoutManager = LinearLayoutManager(requireContext())
        buttonList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allLists.observe(viewLifecycleOwner) { lists ->
            adapter.submitList(lists)
        }
    }
    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddButtonDialogFragment { newListName ->
                viewModel.addList(newListName)
            }.show(parentFragmentManager, "AddListDialog")
        }
    }

    private fun openTodoList(item: ButtonListEntry) {
        val bundle = Bundle().apply {
            putLong("listId", item.id)
            putString("listName", item.title)
        }

        val navController = requireActivity()
            .findNavController(R.id.nav_host_fragment_content_main)

        navController.navigate(R.id.actionMainListFragmentToTodoListFragment, bundle)
    }
}