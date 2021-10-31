package com.example.notesappRoom_LiveData

import androidx.recyclerview.widget.DiffUtil

class MyDiffUtil(
    private val oldList:List<Note>,
    private val newList:ArrayList<Note>

):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
    return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //compare the pk
     return oldList[oldItemPosition].id==newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return when{
           oldList[oldItemPosition].id!=newList[newItemPosition].id->false
           oldList[oldItemPosition].note!=newList[newItemPosition].note->false
           oldList[oldItemPosition].color!=newList[newItemPosition].color->false
           else->true
       }
    }
}