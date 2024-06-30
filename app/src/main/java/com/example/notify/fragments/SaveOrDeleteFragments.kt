package com.example.notify.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notify.R
import com.example.notify.activities.MainActivity
import com.example.notify.adapters.ColorAdapter
import com.example.notify.databinding.BottomSheetBinding
import com.example.notify.databinding.FragmentSaveOrDeleteFragmentsBinding
import com.example.notify.models.NoteEntity
import com.example.notify.utils.hideKeyboard
import com.example.notify.viewmodel.NoteMainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class SaveOrDeleteFragments : Fragment(R.layout.fragment_save_or_delete_fragments) {
    private lateinit var navController: NavController
    private lateinit var contentBinding: FragmentSaveOrDeleteFragmentsBinding
    private var note: NoteEntity? = null
    private var color = -1
    lateinit var result: String
    private val noteActivityViewModel: NoteMainViewModel by viewModels()
    private val currentDate = SimpleDateFormat.getInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private val args: SaveOrDeleteFragmentsArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            scrimColor = Color.TRANSPARENT
            duration = 300L
        }
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding = FragmentSaveOrDeleteFragmentsBinding.bind(view)
        navController = Navigation.findNavController(view)
        val activity = activity as MainActivity

        ViewCompat.setTransitionName(
            contentBinding.noteContentFragmentParent,
            "recyclerView_${args.note?.id}"
        )

        contentBinding.backBtn.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }

        contentBinding.saveNote.setOnClickListener {
            saveNote()
        }

        try {
            contentBinding.etNoteContent.setOnFocusChangeListener { _, focused ->
                if (focused) {
                    contentBinding.bottomBar.visibility = View.VISIBLE
                    contentBinding.etNoteContent.setStylesBar(contentBinding.styleBar)
                } else contentBinding.bottomBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        contentBinding.fabColorPick.setOnClickListener {
            showColorPickerBottomSheet()
        }

        setUpNote()
    }

    private fun showColorPickerBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialogTheme
        )
        val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val bottomSheetBinding = BottomSheetBinding.bind(bottomSheetView)

        val colors = resources.getIntArray(R.array.color_picker).toList()
        val colorAdapter = ColorAdapter(colors) { selectedColor ->
            color = selectedColor
            updateColors(selectedColor)
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.colorRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        bottomSheetView.post {
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun updateColors(selectedColor: Int) {
        contentBinding.apply {
            noteContentFragmentParent.setBackgroundColor(selectedColor)
            toolbarFragmentNoteContent.setBackgroundColor(selectedColor)
            bottomBar.setBackgroundColor(selectedColor)
        }
        activity?.window?.statusBarColor = selectedColor
    }

    private fun setUpNote() {
        val note = args.note
        val title = contentBinding.etTitle
        val content = contentBinding.etNoteContent
        val lastEdited = contentBinding.LastEdited
        if (note == null) {
            contentBinding.LastEdited.text =
                getString(R.string.edited_on, SimpleDateFormat.getDateInstance().format(Date()))
        }
        if (note != null) {
            title.setText(note.title)
            content.renderMD(note.content)
            color = note.color
            lastEdited.text = getString(R.string.edited_on, note.date)
            contentBinding.apply {
                job.launch {
                    delay(10)
                    noteContentFragmentParent.setBackgroundColor(color)
                }
                toolbarFragmentNoteContent.setBackgroundColor(color)
                bottomBar.setBackgroundColor(color)
            }
            activity?.window?.statusBarColor = note.color
        }
    }

    private fun saveNote() {
        if (contentBinding.etNoteContent.text.toString()
                .isEmpty() || contentBinding.etTitle.text.toString().isEmpty()
        ) {
            Toast.makeText(activity, "Something is Empty", Toast.LENGTH_SHORT).show()
        } else {
            note = args.note
            when (note) {
                null -> {
                    noteActivityViewModel.saveNote(
                        NoteEntity(
                            0,
                            contentBinding.etTitle.text.toString(),
                            contentBinding.etNoteContent.getMD(),
                            currentDate,
                            color
                        )
                    )
                    result = "Note Saved"
                    setFragmentResult("key", bundleOf("bundleKey" to result))
                    navController.navigate(SaveOrDeleteFragmentsDirections.actionSaveOrDeleteFragmentsToNoteFragments())
                }
                else -> {
                    updateNote()
                    navController.popBackStack()
                }
            }
        }
    }

    private fun updateNote() {
        if (note != null) {
            noteActivityViewModel.updateNote(
                NoteEntity(
                    note!!.id,
                    contentBinding.etTitle.text.toString(),
                    contentBinding.etNoteContent.getMD(),
                    currentDate,
                    color
                )
            )
        }
    }
}