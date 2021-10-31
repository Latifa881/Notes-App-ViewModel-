package com.example.notesappRoom_LiveData


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDB by lazy { NoteDatabase.getInstance(application).NoteDao() }
    private var notes: LiveData<List<Note>> = noteDB.getAllNotesInfo()


    fun getAllNotes(): LiveData<List<Note>> {
        return notes
    }

    fun addNote(noteObj: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDB.insertNote(noteObj)
        }

    }

    fun updateNote(noteObj: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDB.update(noteObj)
        }

    }

    fun deleteNote(noteObj: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDB.delete(noteObj)
        }
       // getAllNotes()
    }
}