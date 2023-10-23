package com.example.mvc_app.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvc_app.R
import com.example.mvc_app.Utils.TaskitemClickListner
import com.example.mvc_app.localdata.EntityTask

class TasksRecyclerAdapter(var taskitemClickListner: TaskitemClickListner):ListAdapter<EntityTask,TasksRecyclerAdapter.MyViewHolder>(MyDifUtil()){


    inner class MyViewHolder(binding: View): RecyclerView.ViewHolder(binding){
        var taskname:TextView  = binding.findViewById(R.id.titleTask)
        var clock :TextView   = binding.findViewById(R.id.clock)
        var taskcheck:RadioButton   = binding.findViewById(R.id.taskcheked)
        var item  :LinearLayout = binding.findViewById(R.id.item)
        var color :LinearLayout   = binding.findViewById(R.id.tasklistcolor)

        fun OnBind(entityTask: EntityTask){
            taskname.text  =  entityTask.taskTitle
            clock.text  = entityTask.taskHour
            taskcheck.isChecked  = entityTask.checkDo!!
            color.setBackgroundColor(Color.parseColor(entityTask.listColor))
            if(entityTask.checkDo!!){
                item.isEnabled  = false
            }

            taskcheck.setOnClickListener {
                var varible  =  entityTask
                varible.checkDo  = true
                taskitemClickListner.taskitemClickListner(varible)
            }
            item.setOnLongClickListener(object :OnLongClickListener{
                override fun onLongClick(p0: View?): Boolean {
                    taskitemClickListner.longPressListner(entityTask)
                    return true
                }
            })
        }

    }


    class MyDifUtil(): DiffUtil.ItemCallback<EntityTask>() {
        override fun areItemsTheSame(oldItem: EntityTask, newItem: EntityTask): Boolean {
            return oldItem.taskid==newItem.taskid
        }

        override fun areContentsTheSame(oldItem: EntityTask, newItem: EntityTask): Boolean {

            return oldItem==newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.OnBind(getItem(position))
    }




}