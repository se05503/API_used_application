package com.example.assignment_hard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.StoreLayoutBinding
import java.text.SimpleDateFormat

class StoreAdapter(private var items: ArrayList<DocumentResponse>, val context: Context) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    var itemClickRemove: StoreItemDeleteListener? = null

    class ViewHolder(val binding: StoreLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(dateFormat)

        fun bind(item: DocumentResponse) {
            Log.d("item", item.toString()) // 이 로그캣 진짜 도움 많이 됨
            Glide.with(context)
                .load(item.thumbnailUrl)
                .into(binding.ivPerson)
            binding.tvSitename.text = item.displaySitename
            binding.tvDatetime.text = simpleDateFormat.format(item.datetime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 인터페이스 객체화
        if(StoreFragment.getFragment() is StoreItemDeleteListener) {
            itemClickRemove = StoreFragment.getFragment()
        } else {
            throw RuntimeException("must implement Interface")
        }

        val binding =
            StoreLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        Log.d("item_size", items.size.toString())
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            Log.d("isClicked?",position.toString())
            itemClickRemove?.deleteItem(it,position) // 참고 코드에서는 view를 받긴 하는데, 안쓰는 것 같아서 일단 position만 받음
        }
    }

    fun update(updateItem : ArrayList<DocumentResponse>) {
        items = updateItem
        Log.d("updateItem",items.toString())
        notifyDataSetChanged() // 어댑터에게 데이터가 갱신되었다고 알림 → 바로 갱신
    }
}