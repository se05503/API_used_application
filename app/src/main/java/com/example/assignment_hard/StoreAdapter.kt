package com.example.assignment_hard

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
        var itemClickRemove: StoreItemDeleteListener? = null

        fun bind(item: DocumentResponse) {
            Log.d("item", item.toString())
            itemView.setOnClickListener {
                Log.d("invisible click!", "clikced")
            }
            Glide.with(context)
                .load(item.thumbnailUrl)
                .into(binding.ivPerson)
            binding.tvSitename.text = item.displaySitename
            binding.tvDatetime.text = simpleDateFormat.format(item.datetime)
        }
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("경고 메세지")
        builder.setMessage("정말 이미지를 보관함에서 지우시겠습니까?")
        builder.setIcon(R.drawable.ic_trashcan)

        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {

                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    null
                }
            }
        }

        builder.setPositiveButton("삭제", listener)
        builder.setNegativeButton("취소", listener)
        builder.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
//        holder.itemView.setOnClickListener {
//            itemClickRemove?.deleteItem(position) // 참고 코드에서는 view를 받긴 하는데, 안쓰는 것 같아서 일단 position만 받음
//            notifyDataSetChanged()
//        }
    }
}