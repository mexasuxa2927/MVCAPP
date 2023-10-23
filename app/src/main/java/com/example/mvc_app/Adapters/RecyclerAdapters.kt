package com.example.mvc_app.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvc_app.R
import com.example.mvc_app.Utils.ItemClickListner
import com.example.mvc_app.localdata.EntityLists

class RecyclerAdapters(var itemClickListner: ItemClickListner):ListAdapter<EntityLists,RecyclerAdapters.MyViewHolder>(DifUtil()){


    inner class MyViewHolder(binding: View): RecyclerView.ViewHolder(binding){
        var background:LinearLayout  =itemView.findViewById(R.id.background)
        var textName :TextView   = itemView.findViewById(R.id.taskListName)
        var tasksnumber :TextView  = itemView.findViewById(R.id.taskNumber)

        @SuppressLint("SetTextI18n")
        fun OnBind(entityLists: EntityLists){
            background.setBackgroundColor(Color.parseColor(entityLists.listColor))
            textName.setText(entityLists.listTitile)
            tasksnumber.setText("${entityLists.numberofTasks} task")

            background.setOnClickListener{
                itemClickListner.itemClickListner(entityLists)
            }
        }
    }
    class DifUtil(): DiffUtil.ItemCallback<EntityLists>() {
        override fun areItemsTheSame(oldItem: EntityLists, newItem: EntityLists): Boolean {
            return oldItem.taskid==newItem.taskid
        }

        override fun areContentsTheSame(oldItem: EntityLists, newItem: EntityLists): Boolean {

            return oldItem==newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listitem,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.OnBind(getItem(position))
    }


}