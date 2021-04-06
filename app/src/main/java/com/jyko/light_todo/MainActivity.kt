package com.jyko.light_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jyko.light_todo.databinding.ActivityMainBinding
import com.jyko.light_todo.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {

    // 작업을 위한 데이터 생성
    private val data = arrayListOf<Todo>()

    // binding 변수 선언
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 에 레이아웃 연결
        binding = ActivityMainBinding.inflate(layoutInflater)
        //binding에 루트뷰 참조
        val view = binding.root
        setContentView(view)

        data.add(Todo("test1"))
        data.add(Todo("test2"))

        // 리사이클러뷰 객체 지정
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = TodoAdapter(data)

    }

    private fun addTodo(){
        val todo = Todo(binding.edtiText.text.toString())


    }
}


class TodoAdapter(private val dataSet: List<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(var binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        //view 객체가 아닌 바인딩 객체를 넣어줌.

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TodoViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_todo, viewGroup, false)

        return TodoViewHolder(ItemTodoBinding.bind(view))
    }


    override fun onBindViewHolder(viewHolder: TodoViewHolder, position: Int) {

        viewHolder.binding.todoText.text = dataSet[position].text
    }


    override fun getItemCount() = dataSet.size

}
