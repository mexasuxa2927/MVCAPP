package com.example.mvc_app.localdata

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [EntityLists::class,EntityTask::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getTaskDao():TaskDao
    abstract fun getLIstDao():ListDao

}