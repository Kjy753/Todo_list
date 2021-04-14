package com.jyko.light_todo

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jyko.light_todo.databinding.ActivityMainBinding
import com.jyko.light_todo.databinding.ItemTodoBinding



class MainActivity : AppCompatActivity() {

    // binding 변수 선언
    private lateinit var binding: ActivityMainBinding
    // viewModel 선언
    private val viewmodel:MainVIewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 에 레이아웃 연결
        binding = ActivityMainBinding.inflate(layoutInflater)
        //binding에 루트뷰 참조
        val view = binding.root
        setContentView(view)



        // 리사이클러뷰 객체 지정 apply를 통해 중복되는 부분 제거
         binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
             adapter = TodoAdapter(
                 viewmodel.data,
                onClickDeleteIcon = {
                   viewmodel.deleteTodo(it)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                },
                onClickItem = {
                    viewmodel.toggleTodo(it)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            )
        }

       //추가버튼 이벤트
        binding.addButton.setOnClickListener {
            val todo = Todo(binding.editText.text.toString())
            viewmodel.addTodo(todo)
            binding.recyclerView.adapter?.notifyDataSetChanged()

        }

    }

}


class TodoAdapter(
    private val dataSet: List<Todo>,
    val onClickDeleteIcon :(todo : Todo) -> Unit,
    val onClickItem :(todo : Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(var binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        //view 객체가 아닌 바인딩 객체를 넣어줌.
    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TodoViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_todo, viewGroup, false)

        return TodoViewHolder(ItemTodoBinding.bind(view))
    }


    override fun onBindViewHolder(viewHolder: TodoViewHolder, position: Int) {

            // todo라는 이름의 포지션 선언
            val todo = dataSet[position]

                // 객체의 text를 itemlist의 todoText에 할당
                viewHolder.binding.todoText.text = todo.text

                // 객체의 delteIcon 함수를 invoke를 이용해 실행
                viewHolder.binding.deleteImageView.setOnClickListener {
                    onClickDeleteIcon.invoke(todo)
                }

                viewHolder.binding.root.setOnClickListener {
                    onClickItem.invoke(todo)
                }

                // 할일 완료시 완료선 긋기작업
                if (todo.isDone) {
                    viewHolder.binding.todoText.apply {
                        paintFlags =
                            viewHolder.binding.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        setTypeface(null, Typeface.ITALIC)
                    }
                } else {
                    viewHolder.binding.todoText.apply {
                        paintFlags = 0
                        setTypeface(null, Typeface.NORMAL)
                    }
                }

    }

    override fun getItemCount() = dataSet.size

}
