package com.example.mvc_app.localdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserTask(entityTask: EntityTask)

    @Update
    suspend fun updateTask(entityTask: EntityTask)

    @Delete
    suspend fun deleteTask(entityTask: EntityTask)

    @Query("SELECT * FROM Tasks WHERE listTitile   = :tasktitle")
    fun  readDataWithTitle(tasktitle:String) :LiveData<List<EntityTask>>

    @Query("SELECT * FROM Tasks WHERE taskDay   = :taskDay")
    fun  readDataByDay(taskDay:String) :LiveData<List<EntityTask>>

    @Query("DELETE  FROM Tasks WHERE listTitile   = :tasktitle AND tasklistId = :tasklistId")
    suspend fun deleteWhichTitle(tasktitle: String,tasklistId:String)

}