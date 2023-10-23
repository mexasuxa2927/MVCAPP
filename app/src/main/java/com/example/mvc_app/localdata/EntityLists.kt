package com.example.mvc_app.localdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Lists")
data class EntityLists(
    @PrimaryKey(autoGenerate = true)
    var taskid:Int?=null,
    var listTitile:String,
    var listColor:String,
    var numberofTasks:Int=0
    ) {

}