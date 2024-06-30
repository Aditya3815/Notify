package com.example.notify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notify.databinding.NoteItemBinding
import com.example.notify.fragments.NoteFragmentsDirections
import com.example.notify.models.NoteEntity
import com.example.notify.utils.hideKeyboard
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.commonmark.node.SoftLineBreak

class NotesAdapter : ListAdapter<NoteEntity, NotesAdapter.NotesViewHolder>(DiffUtilCallback) {



    inner class NotesViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title: MaterialTextView = binding.noteItemTitle
        private val content: TextView = binding.noteContentItem
        private val date: MaterialTextView = binding.noteDate
        private val parent: MaterialCardView = binding.noteItemLayoutParent
        private val markWon = Markwon.builder(itemView.context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(itemView.context))
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    super.configureVisitor(builder)
                    builder.on(SoftLineBreak::class.java) { visitor, _ -> visitor.forceNewLine() }
                }
            }).build()

        fun bind(note: NoteEntity) {
            title.text = note.title
            content.text = note.content
            markWon.setMarkdown(content, note.content)
            date.text = note.date
            parent.setCardBackgroundColor(note.color)
            parent.transitionName = "recyclerView_${note.id}"
            itemView.setOnClickListener {

                val action = NoteFragmentsDirections.actionNoteFragmentsToSaveOrDeleteFragments(note)
                val extras =  FragmentNavigatorExtras(parent to "recyclerView_${note.id}")
                it.hideKeyboard()
                Navigation.findNavController(it).navigate(action, extras)
            }
            content.setOnClickListener{
                val action = NoteFragmentsDirections.actionNoteFragmentsToSaveOrDeleteFragments(note)
                val extras =  FragmentNavigatorExtras(parent to "recyclerView_${note.id}")
                it.hideKeyboard()
                Navigation.findNavController(it).navigate(action, extras)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    companion object {
        val DiffUtilCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}