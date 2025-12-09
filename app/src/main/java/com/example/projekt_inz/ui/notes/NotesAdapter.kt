package com.example.projekt_inz.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R

class NotesAdapter(
    private val onClick: (NoteEntity) -> Unit,
    private val onEditClick: (NoteEntity) -> Unit,
    private val onDeleteClick: (NoteEntity) -> Unit
) : ListAdapter<NoteEntity, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)

        holder.noteText.text = note.text

        // FULL ITEM CLICK (→ show full note)
        holder.itemView.setOnClickListener { onClick(note) }

        // OPTIONAL: if your item_note.xml has edit/delete buttons
//        holder.editButton?.setOnClickListener { onEditClick(note) }
//        holder.deleteButton?.setOnClickListener { onDeleteClick(note) }
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteText: TextView = itemView.findViewById(R.id.item_noteText)
//        val editButton: ImageView = itemView.findViewById(R.id.editB)
//        val deleteButton: ImageView = itemView.findViewById(R.id.deleteB)

//        init {
//            // CLICK on entire item
//            itemView.setOnClickListener {
//                val pos = absoluteAdapterPosition
//                if (pos != RecyclerView.NO_POSITION) {
//                    onClick(getItem(pos))
//                }
//            }
//        }
//
//        fun bind(entry: NoteEntity) {
//
//            noteText.text = entry.text
//
////            editButton.setOnClickListener {
////                onEdit(entry)
////            }
////
////            deleteButton.setOnClickListener {
////                onDelete(entry)
////            }
//        }
    }
}

class NoteDiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
    override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem.text == newItem.text
    }

}