package com.example.notify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
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

    companion object {
        private const val TRANSITION_NAME_PREFIX = "recyclerView_"

        val DiffUtilCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var markwon: Markwon

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        markwon = Markwon.builder(recyclerView.context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(recyclerView.context))
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    super.configureVisitor(builder)
                    builder.on(SoftLineBreak::class.java) { visitor, _ -> visitor.forceNewLine() }
                }
            }).build()
    }

    inner class NotesViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title: MaterialTextView = binding.noteItemTitle
        private val content: TextView = binding.noteContentItem
        private val date: MaterialTextView = binding.noteDate
        private val parent: MaterialCardView = binding.noteItemLayoutParent

        fun bind(note: NoteEntity) {
            title.text = note.title
            markwon.setMarkdown(content, note.content)
            date.text = note.date
            parent.setCardBackgroundColor(note.color)
            parent.transitionName = TRANSITION_NAME_PREFIX + note.id

            val clickListener = View.OnClickListener {
                val action = NoteFragmentsDirections.actionNoteFragmentsToSaveOrDeleteFragments(note)
                val extras = FragmentNavigatorExtras(parent to parent.transitionName)
                it.hideKeyboard()
                Navigation.findNavController(it).navigate(action, extras)
            }

            itemView.setOnClickListener(clickListener)
            content.setOnClickListener(clickListener)
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
}