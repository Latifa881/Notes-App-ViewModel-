package com.example.notesappRoom_LiveData

import androidx.lifecycle.LiveData
import androidx.room.*

//Data Access Object
@Dao
interface NoteDao {

    @Query("SELECT * FROM Note ")
    fun getNotesInfo(): List<Note>
    @Query("SELECT * FROM Note ")
    fun getAllNotesInfo(): LiveData<List<Note>>

    @Insert
  suspend fun insertNote(note: Note)
    @Query("DELETE FROM Note where Id=:id")
    fun deleteNote(id: Int)
    @Query("UPDATE Note SET Note=:note, Color=:color WHERE Id=:id")
    fun updateNote(id:Int,note: String,color: String)
    @Update
    fun update(note:Note)
    @Delete
    fun delete(note:Note)


}