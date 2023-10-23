package com.example.mvc_app.localdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class EntityTask(
    @PrimaryKey(autoGenerate = true)
    var taskid:Int?,
    var checkDo:Boolean?,
    var taskTitle:String,
    var taskDay:String,
    var taskHour:String,
    var tasklistId:Int,
    var listTitile:String,
    var listColor:String

   ) {




}