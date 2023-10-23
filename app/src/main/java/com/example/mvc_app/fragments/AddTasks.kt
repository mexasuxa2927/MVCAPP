package com.example.mvc_app.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.mvc_app.Adapters.RecyclerAdapters
import com.example.mvc_app.R
import com.example.mvc_app.Utils.ItemClickListner
import com.example.mvc_app.databinding.FragmentAddTasksBinding
import com.example.mvc_app.localdata.EntityLists
import com.example.mvc_app.localdata.EntityTask
import com.example.mvc_app.localdata.ListDao
import com.example.mvc_app.localdata.MyDatabase
import com.example.mvc_app.localdata.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTasks.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTasks : Fragment(),ItemClickListner {


    lateinit var binding :FragmentAddTasksBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var listDao: ListDao
    lateinit var adapters: RecyclerAdapters
    lateinit var taskDao: TaskDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var selectedList :EntityLists
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentAddTasksBinding.inflate(layoutInflater,container,false)
        binding.soat.minValue  = 0
        binding.soat.maxValue  = 23
        binding.minut.maxValue   = 59
        binding.minut.minValue  = 0
        binding.minut.textSize = 55f
        binding.soat.textSize  = 55f
        listDao   =Room.databaseBuilder(requireContext(),MyDatabase::class.java,"my.db").build().getLIstDao()
        taskDao  = Room.databaseBuilder(requireContext(),MyDatabase::class.java,"my.db").build().getTaskDao()
        adapters  = RecyclerAdapters(this)
        binding.tasksView.layoutManager  = LinearLayoutManager(requireContext())
        binding.tasksView.adapter  =  adapters
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        setAlldataForRecyclerView()
        initUIs()

        calendarViewClick()
        timepickFun()
        binding.doneButton.setOnClickListener {
            addTaskk()

        }

        return binding.root
    }

    private fun addTaskk() {
        binding.apply {
            if(!dateText.equals("date")&&!timeText.equals("clock")&&binding.taskName.text.trim().toString().isNotEmpty()){
                var entityTask :EntityTask   = EntityTask(null,false,binding.taskName.text.trim().toString(),binding.dateText.text.toString(),timeText.text.toString(),selectedList.taskid!!,selectedList.listTitile,selectedList.listColor)
                GlobalScope.launch(Dispatchers.IO) {
                    taskDao.inserTask(entityTask)
                    var updatedList  = selectedList
                    updatedList.numberofTasks  = updatedList.numberofTasks+1
                    listDao.updateList(updatedList)

                }
                findNavController().popBackStack()
            }
            else{
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun timepickFun() {
        binding.apply {
            var time :String
            soat.setOnValueChangedListener { numberPicker, i, i2 ->
                var soats :String
                if(i2<10){
                    soats  = "0$i2"
                }
                else
                {
                    soats = "$i2"
                }
                var min:String
                if(minut.value<10){
                    min  = "0${minut.value}"
                }
                else{
                    min = "${minut.value}"
                }
                time = soats+":"+min
                binding.timeText.text   = time
            }

            minut.setOnValueChangedListener { numberPicker, i, i2 ->

                var soats :String
                if(i2<10){
                    soats  = "0$i2"
                }
                else
                {
                    soats = "$i2"
                }
                var min:String
                if(soat.value<10){
                    min  = "0${soat.value}"
                }
                else{
                    min = "${soat.value}"
                }
                time = min+":"+soats
                binding.timeText.text   = time
            }


        }
    }

    private fun calendarViewClick() {
        binding.apply {
            calendarview.setOnDateChangeListener { calendarView, i, i2, i3 ->
                var yerar = i.toString()
                var month  =if(i2+1<10){"0${i2+1}"}else{i2+1}
                var day  =  if(i3<10){"0${i3}"}else{i3}
                val string:String   = yerar+"."+month.toString()+"."+day.toString()
                dateText.text   = string
            }
        }
    }


    private fun initUIs() {
       binding.apply {
           calendar.setOnClickListener {
                calendarview.visibility  =View.VISIBLE
                tasksView.visibility  = View.GONE
                alarmview.visibility  = View.GONE
           }
           alarm.setOnClickListener {
               alarmview.visibility  = View.VISIBLE
               tasksView.visibility  = View.GONE
               calendarview.visibility  = View.GONE
           }
           task.setOnClickListener {
               alarmview.visibility  = View.GONE
               tasksView.visibility  = View.VISIBLE
               calendarview.visibility  = View.GONE
           }
       }
    }

    private fun setAlldataForRecyclerView() {
        listDao.getLists().observe(requireActivity(), Observer {
            adapters.submitList(it)
            binding.listname.setText(it[0].listTitile)
            binding.listcolor.setBackgroundColor(Color.parseColor(it[0].listColor))
            selectedList  =  it[0]
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addTasks.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTasks().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun itemClickListner(entityLists: EntityLists) {
        binding.apply {
            listname.text   =entityLists.listTitile
            listcolor.setBackgroundColor(Color.parseColor(entityLists.listColor))
            selectedList  = entityLists
        }
    }
}