package com.example.notesappRoom_LiveData

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialogue_view_add.view.alertLayout
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbBlue
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbGreen
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbRed
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbYellow
import kotlinx.android.synthetic.main.dialogue_view_update_delete.view.*
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecyclerViewAdapter(private var notes: List<Note>,val mainActivity: MainActivity) :
    RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    private val myViewModel by lazy { ViewModelProvider(mainActivity).get(MyViewModel::class.java)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val data = notes[position]
        holder.itemView.apply {
            NoteDatabase.getInstance(context)
            tvNote.text = data.note
            when (data.color) {
                "Red" -> linearLayout.setBackgroundResource(R.drawable.round_layout_red)
                "Blue" -> linearLayout.setBackgroundResource(R.drawable.round_layout_blue)
                "Green" -> linearLayout.setBackgroundResource(R.drawable.round_layout_green)
                "Yellow" -> linearLayout.setBackgroundResource(R.drawable.round_layout_yellow)
            }
            ivOptions.setOnClickListener {

                //Toast.makeText(context,"data.id=${data.id}",Toast.LENGTH_SHORT).show()
                var colorText=data.color
                val builder = AlertDialog.Builder(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_view_update_delete, null)
                //Set the background color
                changeBackGroundColor(data.color, dialogView)
                builder.setView(dialogView)
                val alertDialog: AlertDialog = builder.create()
                dialogView.etYourNote.setText(data.note)
                dialogView.colorsRadioGroupUpdateDelete.setOnCheckedChangeListener { group, checkedId ->
                    colorText=  changeBackGroundColor(checkedId, dialogView)

                }
                dialogView.btUpdateNote.setOnClickListener {
                    val updatedNote = dialogView.etYourNote.text.toString()
                    if (updatedNote.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
//                            NoteDatabase.getInstance(context).NoteDao()
//                                .update(Note(data.id,updatedNote, colorText))
                            myViewModel.updateNote(Note(data.id,updatedNote, colorText))

                        }
                        Toast.makeText(context,"Updated successfully.",Toast.LENGTH_SHORT).show()
                        alertDialog.dismiss()
                      //  mainActivity.readFromDB()

                    } else {
                        Toast.makeText(context,"You need to enter a note!",Toast.LENGTH_SHORT).show()
                    }

                }
                dialogView.btDeleteNote.setOnClickListener {
                    val deleteBuilder = AlertDialog.Builder(context)
                    deleteBuilder.setTitle("Delete note")
                    deleteBuilder.setMessage("Are you sure you want to delete this note?")
                    deleteBuilder.setIcon(android.R.drawable.ic_dialog_alert)

                    deleteBuilder.setPositiveButton("Delete"){dialogInterface, which ->
                        CoroutineScope(Dispatchers.IO).launch {
//                            NoteDatabase.getInstance(context).NoteDao()
//                                .delete(Note(data.id,data.note,data.color))
                            myViewModel.deleteNote(Note(data.id,data.note,data.color))

                        }
                        alertDialog.dismiss()
                       // mainActivity.readFromDB()
                        dialogInterface.dismiss()
                    }
                    //performing cancel action
                    deleteBuilder.setNeutralButton("Cancel"){dialogInterface , which ->
                        dialogInterface.dismiss()
                    }
                    deleteBuilder.setCancelable(true)
                    deleteBuilder.show()
                }

                alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.setCancelable(true)
                alertDialog.show()
            }

        }
    }

    override fun getItemCount() = notes.size
    private fun changeBackGroundColor(colorText: String, dialogView: View) {
        when (colorText) {
            "Red" -> {
                dialogView.rbRed.isChecked = true
                dialogView.rbBlue.isChecked = false
                dialogView.rbGreen.isChecked = false
                dialogView.rbYellow.isChecked = false
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_red)
            }
            "Blue" -> {
                dialogView.rbRed.isChecked = false
                dialogView.rbBlue.isChecked = true
                dialogView.rbGreen.isChecked = false
                dialogView.rbYellow.isChecked = false

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_blue)
            }
            "Green" -> {
                dialogView.rbRed.isChecked = false
                dialogView.rbBlue.isChecked = false
                dialogView.rbGreen.isChecked = true
                dialogView.rbYellow.isChecked = false
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_green)
            }
            "Yellow" -> {
                dialogView.rbRed.isChecked = false
                dialogView.rbBlue.isChecked = false
                dialogView.rbGreen.isChecked = false
                dialogView.rbYellow.isChecked = true
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_yellow)
            }

        }

    }
    private fun changeBackGroundColor(checkedId: Int, dialogView: View): String {
        var colorText = "Red"
        when (checkedId) {
            R.id.rbRed -> {
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_red)
                colorText = "Red"
            }
            R.id.rbBlue -> {

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_blue)
                colorText = "Blue"
            }
            R.id.rbGreen -> {

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_green)
                colorText = "Green"
            }
            R.id.rbYellow -> {

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_yellow)
                colorText = "Yellow"
            }

        }
        return colorText
    }
    fun update(notes: List<Note>){
        println("UPDATING DATA")
        this.notes = notes
        notifyDataSetChanged()
    }
    fun updateRVData(newDetailsList:ArrayList<Note>){
        val diffUtil=MyDiffUtil(notes,newDetailsList)
        val diffResult= DiffUtil.calculateDiff(diffUtil)
        notes=newDetailsList
        diffResult.dispatchUpdatesTo(this)
    }

}
