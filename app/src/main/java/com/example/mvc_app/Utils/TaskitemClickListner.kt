package com.example.mvc_app.Utils

import com.example.mvc_app.localdata.EntityTask

interface TaskitemClickListner {
    fun taskitemClickListner(entityTask: EntityTask)
    fun longPressListner(entityTask: EntityTask)

}