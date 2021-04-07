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
        binding.recyclerView.adapter = TodoAdapter(data,
            onClickDeleteIcon = {
                deleteTodo(it)
            }
            )

        //추가버튼 이벤트
        binding.addButton.setOnClickListener {
            addTodo()
        }


    }

    // 할일 추가 함수
    private fun addTodo(){
        val todo = Todo(binding.edtiText.text.toString())
        data.add(todo)

        // 데이터 입력후 리사이클러뷰에 데이터 변경을 알려줌.
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    // 할일 삭제 함수
    private fun deleteTodo(todo: Todo){
        data.remove(todo)
        binding.recyclerView.adapter?.notifyDataSetChanged()

    }
}


class TodoAdapter(
    private val dataSet: List<Todo>,
    val onClickDeleteIcon :(todo : Todo) -> Unit
) :
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
        val todo = dataSet[position]

        viewHolder.binding.todoText.text = todo.text
        viewHolder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }

    }


    override fun getItemCount() = dataSet.size

}
