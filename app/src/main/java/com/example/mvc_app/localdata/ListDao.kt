package com.example.mvc_app.localdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListDao {



    @Query("SELECT * FROM Lists")
    fun getLists():LiveData<List<EntityLists>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(entityLists: EntityLists)


    @Update
    suspend fun updateList(entityLists: EntityLists)

    @Query("SELECT * FROM Lists Where taskid = :taskid")
    suspend fun getListByID(taskid:Int):EntityLists


}