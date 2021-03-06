package com.jyko.light_todo

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
                 emptyList(),
                onClickDeleteIcon = {
                   viewmodel.deleteTodo(it)
                    //binding.recyclerView.adapter?.notifyDataSetChanged()
                },
                onClickItem = {
                    viewmodel.toggleTodo(it)
                    //binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            )
        }

       //추가버튼 이벤트
        binding.addButton.setOnClickListener {
            val todo = Todo(binding.editText.text.toString())
            viewmodel.addTodo(todo)
            // setData 함수로 대체
            //binding.recyclerView.adapter?.notifyDataSetChanged()

        }

        // 관찰 UI 업데이트
        // 관찰 가능한 todoLiveData.Observe
        viewmodel.todoLiveData.observe(this, Observer {
            //데이터의 변경이 일어날때마다 실행 ex) MainVIewModel.kt 안에 addTodo 밑에 Livedata.value = data 실행시마다
            // 우리가 사용하는 TodoAdapter로 캐스팅.setData(List<Todo>) 를 통해 업데이트!
            (binding.recyclerView.adapter as TodoAdapter).setData(it)
        })

    }

}


class TodoAdapter(
    private var dataSet: List<Todo>,
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

    //MutableLiveData 타입의 todoLiveData 를 이용해 데이터가 변경되는것 감지해야 해서 set 함수를 어댑터에 새로 작성
    fun setData(newData:List<Todo>){
        dataSet = newData
        // 데이터가 바뀔떄마다 위 메소드를 실행하면 데이터를 업데이트 해주고 notify를 여기서 해줌
        notifyDataSetChanged()
    }
}
