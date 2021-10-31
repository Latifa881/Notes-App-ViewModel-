package com.example.notesappRoom_LiveData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class],version = 1,exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    companion object{
        var instance:NoteDatabase?=null;
        fun getInstance(ctx: Context):NoteDatabase
        {
            if(instance!=null)
            {
                return  instance as NoteDatabase;
            }
            instance= Room.databaseBuilder(ctx,NoteDatabase::class.java,"note").run { allowMainThreadQueries() }.build()
            return instance as NoteDatabase;
        }
    }
    abstract fun NoteDao():NoteDao;
}