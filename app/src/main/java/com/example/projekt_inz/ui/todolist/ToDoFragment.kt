package com.example.projekt_inz.ui.todolist

import android.media.SoundPool
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoFragment : Fragment() {

    private lateinit var viewModel: ToDoViewModel
    private lateinit var taskList: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var pointsCounter: TextView
    private var fanfarePlayed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)

        val db = TaskDatabase.getDatabase(requireContext())
        val scoreRepo = ScoreRepository(db.scoreDao())

        val factory = ToDoViewModelFactory(repository, scoreRepo)

        findViews(view)

        viewModel = ViewModelProvider(this, factory)[ToDoViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupAddButton()
    }

    private fun findViews(view: View) {
        taskList = view.findViewById(R.id.taskList)
        addButton = view.findViewById(R.id.addButton)
        pointsCounter = view.findViewById(R.id.pointsCounter)
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onEdit = { task ->
                EditTaskDialogFragment(task.text) { newText ->
                    viewModel.updateTask(task, newText)
                }.show(parentFragmentManager, "EditTask")
            },
            onDelete = { task -> viewModel.deleteTask(task) },
            onChecked = { task, checked -> viewModel.toggleTask(task, checked) }
        )

        taskList.layoutManager = LinearLayoutManager(requireContext())
        taskList.adapter = taskAdapter
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)

            if (tasks.isNotEmpty() && tasks.all { it.isDone }) {
                if (!fanfarePlayed) {
                    playFanfare()
                    fanfarePlayed = true
                }
            } else {
                fanfarePlayed = false
            }
        }

        viewModel.score.observe(viewLifecycleOwner) { score ->
            pointsCounter.text = (score?.points ?: 0).toString()
        }
    }

    private fun playFanfare() {
        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        val soundId = soundPool.load(requireContext(), R.raw.fanfare, 1)

        soundPool.setOnLoadCompleteListener { _, _, _ ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddTaskDialogFragment { newTaskText ->
                viewModel.addTask(newTaskText)
            }.show(parentFragmentManager, "AddTaskDialog")
        }
    }
}