package com.example.projekt_inz.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projekt_inz.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.ui.routines.EditButtonDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {

    private lateinit var adapter: NotesAdapter
    private lateinit var viewModel: NotesViewModel
    private lateinit var notesList: RecyclerView
    private lateinit var addButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = NotesDatabase.getDatabase(requireContext())
        val notesDao = db.notesDao()
        val repository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(repository)

        findViews(view)

        viewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupAddButton()

    }

    private fun findViews(view: View) {
        notesList = view.findViewById(R.id.notesList)
        addButton = view.findViewById(R.id.addButton)
    }

    private fun setupRecyclerView() {
        adapter = NotesAdapter(
            onClick = { item ->
                ShowNoteDialogFragment
                .newInstance(item.text)
                .show(childFragmentManager, "ShowNoteDialog")
            },
            onEditClick = { item ->
                EditNoteDialogFragment(item.text) { newText ->
                    viewModel.updateNote(item, newText)
                }.show(parentFragmentManager, "EditNoteDialog")
            },
            onDeleteClick = { item ->
                viewModel.deleteNote(item)
            }
        )
        notesList.layoutManager = LinearLayoutManager(requireContext())
        notesList.adapter = adapter
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddNoteDialogFragment { newNoteText ->
                viewModel.addNote(newNoteText)
            }.show(parentFragmentManager, "AddListDialog")
        }
    }

    private fun observeViewModel() {
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes)
        }
    }

}