package com.example.projekt_inz.ui.routines.routine_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.todolist.AddTaskDialogFragment
import com.example.projekt_inz.ui.todolist.EditTaskDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoListFragment : Fragment() {

    private var listId: Long = 0
    private lateinit var listName: String
    private lateinit var titleTextView: TextView
    private lateinit var taskListR: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var viewModel: TaskRoutinesViewModel
    private lateinit var taskAdapter: TaskRoutinesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listId = arguments?.getLong("listId") ?: 0L
        listName = arguments?.getString("listName") ?: "Unnamed"
        if (listId == 0L) {
            throw IllegalStateException("Invalid listId passed to TodoListFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = view.findViewById(R.id.todoListTitle)
        titleTextView.text = listName

        //val listId = arguments?.getLong("listId") ?: 0

        taskListR = view.findViewById(R.id.taskListR)
        addButton = view.findViewById(R.id.addButton)

        val db = RoutineTaskDatabase.getDatabase(requireContext())
        val taskDao = db.taskDao()
        val repo = TaskRoutinesRepository(taskDao)
        val factory = TaskRoutinesViewMFactory(repo, listId)
        viewModel = ViewModelProvider(this, factory)[TaskRoutinesViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskRoutinesAdapter(
            onEdit = { task ->
                EditTaskDialogFragment(task.text) { newText ->
                    viewModel.updateTask(task, newText)
                }.show(parentFragmentManager, "EditTask")
            },
            onDelete = { task -> viewModel.deleteTask(task) },
            onChecked = { task, checked -> viewModel.toggleTask(task, checked) }
        )

        taskListR.layoutManager = LinearLayoutManager(requireContext())
        taskListR.adapter = taskAdapter
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks) //
        }
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddTaskRFragment(listId) { newTask ->

                    viewModel.addTask(newTask) // pass the entity, not toString()

            }.show(parentFragmentManager, "AddToListDialog")
        }
    }
}