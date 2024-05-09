package com.example.assignment_hard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.StoreLayoutBinding
import java.text.SimpleDateFormat

class StoreAdapter(val items: ArrayList<DocumentResponse>, val context: Context) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {


    class ViewHolder(val binding: StoreLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(dateFormat)

        fun bind(item: DocumentResponse) {
            Log.d("item",item.toString())
            if (item.status == true) {
                Glide.with(context)
                    .load(item.thumbnailUrl)
                    .into(binding.ivPerson)
                binding.tvSitename.text = item.displaySitename
                binding.tvDatetime.text = simpleDateFormat.format(item.datetime)
            } else if(item.status == false) {
                binding.storeRecyclerview.visibility = View.GONE // 당장 문제는 해결 가능하지만, 여전히 recyclerView에 담겨있다는 문제점이 발생 (리소스 낭비)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StoreLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        Log.d("item_size",items.size.toString())
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}