@file:OptIn(DelicateCoroutinesApi::class)

package com.example.mvc_app.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.mvc_app.Adapters.RecyclerAdapters
import com.example.mvc_app.Adapters.TasksRecyclerAdapter
import com.example.mvc_app.R
import com.example.mvc_app.Utils.ItemClickListner
import com.example.mvc_app.Utils.TaskitemClickListner
import com.example.mvc_app.databinding.DialogViewsBinding
import com.example.mvc_app.databinding.FragmentMainPageBinding
import com.example.mvc_app.localdata.EntityLists
import com.example.mvc_app.localdata.EntityTask
import com.example.mvc_app.localdata.ListDao
import com.example.mvc_app.localdata.MyDatabase
import com.example.mvc_app.localdata.TaskDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainPage : Fragment(),ItemClickListner,TaskitemClickListner{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding :FragmentMainPageBinding
    lateinit var myDatabase: MyDatabase
    lateinit var listDao:ListDao
    lateinit var recyclerAdapters: RecyclerAdapters
    lateinit var taskDao:TaskDao
    lateinit var taskadapter:TasksRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentMainPageBinding.inflate(layoutInflater,container,false)
        myDatabase  = Room.databaseBuilder(requireContext(),MyDatabase::class.java,"my.db").build()
        listDao  = myDatabase.getLIstDao()
        recyclerAdapters = RecyclerAdapters(this)
        taskDao  = myDatabase.getTaskDao()
        taskadapter   = TasksRecyclerAdapter(this)


        loadTodayTasks()
        loadDataFirstTime()
        addButtonAction()
        dialogItemClick()
        checkListDatabse()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTodayTasks() {
        binding.dailyTaskRv.layoutManager = LinearLayoutManager(requireContext())
        binding.dailyTaskRv.adapter  = taskadapter
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val current = LocalDateTime.now().format(formatter)

        taskDao.readDataWithTitle("Work").observe(requireActivity(), Observer {
            Log.d("@@@@@", "Taskwith title:$it")
        })


        taskDao.readDataByDay(current).observe(requireActivity(), Observer {

            taskadapter.submitList(it)

        })


    }




    private fun dialogItemClick() {
        binding.apply {
            taskButton.setOnClickListener{
                dialog.visibility =View.GONE
                addButton.setBackgroundColor(Color.WHITE)
                findNavController().navigate(R.id.action_mainPage_to_addTasks)
            }
            listButton.setOnClickListener {
                dialog.visibility  =View.GONE
                addButton.setBackgroundColor(Color.WHITE)
                dialogViewCreate()
            }

        }

    }

    @SuppressLint("ResourceType")
    private fun dialogViewCreate() {
        var alertDialog  = AlertDialog.Builder(requireContext()).create()
        var view  = DialogViewsBinding.inflate(LayoutInflater.from(requireContext()))
        view.apply {
            var color   = requireActivity().getColor(R.color.blue)
            blueBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.blue))
                color   = requireActivity().getColor(R.color.blue) }
            yellowBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.yellow))
                color   = requireActivity().getColor(R.color.yellow)}
            redBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.red))
                color   = requireActivity().getColor(R.color.red)}
            blackBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.black))
                color   = requireActivity().getColor(R.color.black)}
            greenBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.green))
                color   = requireActivity().getColor(R.color.green)}
            grayBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.gray))
                color   = requireActivity().getColor(R.color.gray)}
            purpleBtn.setOnClickListener { submitButton.setBackgroundColor(requireActivity().getColor(R.color.purple))
                color   = requireActivity().getColor(R.color.purple)}


            cancelButton.setOnClickListener { alertDialog.dismiss() }
            submitButton.setOnClickListener {

                if(listname.text.trim().isNotEmpty()){

                    var entityLists  = EntityLists(null,listname.text.trim().toString(),String.format("#%06X", 0xFFFFFF and color))
                    GlobalScope.launch(Dispatchers.IO) {
                        listDao.insertList(entityLists)
                    }
                    alertDialog.dismiss()
                }
            }

        }
        alertDialog.setView(view.root)
        alertDialog.show()
    }

    private fun addButtonAction() {
        binding.apply {
           addButton.setOnClickListener {
                if(dialog.isVisible==false){
                   dialog.visibility  = View.VISIBLE
                    addButton.setBackgroundColor(Color.BLUE)

                }
               else{
                    dialog.visibility = View.GONE
                    addButton.setBackgroundColor(Color.WHITE)
                }

            }

        }

    }

    fun loadDataFirstTime(){

         binding.rvLists.layoutManager  = LinearLayoutManager(requireContext())
         binding.rvLists.adapter = recyclerAdapters

         loadData()
     }

    private fun loadData() {
        listDao.getLists().observe(requireActivity(), Observer {
            recyclerAdapters.submitList(it)
            Log.d("@@@@", "loadData:$it")
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun checkListDatabse() {

        listDao.getLists().observe(requireActivity(), Observer {
            if(it.isEmpty()){
                GlobalScope.launch(Dispatchers.IO) {
                    createList().forEach {
                        listDao.insertList(it)
                    }
                }
            }
        })

    }

    private fun createList():ArrayList<EntityLists> {

        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), com.example.mvc_app.R.color.red))
        var list:ArrayList<EntityLists>  = ArrayList()

        list.add(EntityLists(null,"Inbox",String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), com.example.mvc_app.R.color.gray))  ))
        list.add(EntityLists(null,"Work",String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), com.example.mvc_app.R.color.green))))
        list.add(EntityLists(null,"Shopping",String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), com.example.mvc_app.R.color.red))))
        list.add(EntityLists(null,"Family",String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), com.example.mvc_app.R.color.yellow))))
        list.add(EntityLists(null,"Personal",String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), com.example.mvc_app.R.color.purple))))

        return list
    }

    @SuppressLint("InflateParams")
    override fun itemClickListner(entityLists: EntityLists) {
        val bottomSheet = BottomSheetDialog(requireContext())

        bottomSheet.setContentView(R.layout.bottomsheetview)
        var background: ConstraintLayout? = bottomSheet.findViewById<ConstraintLayout>(R.id.layoutback)
        var titleList : TextView? = bottomSheet.findViewById<TextView>(R.id.titleTaskList)
        var rv  = bottomSheet.findViewById<RecyclerView>(R.id.recyclerView1)
        var adapters1  = TasksRecyclerAdapter(this)
        rv!!.layoutManager   = LinearLayoutManager(requireContext())
        rv.adapter   = adapters1
        background!!.setBackgroundColor(Color.parseColor(entityLists.listColor))
        titleList!!.text  = entityLists.listTitile


        taskDao.readDataWithTitle(entityLists.listTitile).observe(requireActivity(), Observer {
            adapters1.submitList(it)
        })

        bottomSheet.show()


    }



    override fun taskitemClickListner(entityTask: EntityTask) {
       GlobalScope.launch (Dispatchers.IO){
           taskDao.updateTask(entityTask)
       }
    }

    override fun longPressListner(entityTask: EntityTask) {
        GlobalScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(entityTask)
            var tasklist  =  listDao.getListByID(entityTask.tasklistId)
            tasklist.numberofTasks = tasklist.numberofTasks-1
            listDao.updateList(tasklist)
        }

    }
}


