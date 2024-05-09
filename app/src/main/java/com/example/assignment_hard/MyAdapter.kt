package com.example.assignment_hard

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.ItemLayoutBinding
import java.text.SimpleDateFormat

/*
첫번째 오류 해결과정
1. Glide의 with에 context를 받아와하는데 fragment로 잘못 설정했다. → adapter에 변수 하나 더 추가(fragment로부터 context 받아오기)
2. Fragment 의 생명주기 이해 : OnCreate가 아닌 onViewCreated에 설정해야 한다.
 */

class MyAdapter(
    private val data: MutableList<DocumentResponse>,
    private val context: Context
) : RecyclerView.Adapter<MyAdapter.ImageViewHolder>() {

//    interface ItemClick {
//        fun onClick(view:View, position: Int)
//    }
//
//    var itemClick: ItemClick? = null

    class ImageViewHolder(private var binding: ItemLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private val ivHeart = binding.ivHeart
        private var listener: ActivityDataListener? = null

        // SimpleDateFormat
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(dateFormat)
        var currentImage: DocumentResponse? = null

        fun bind(image: DocumentResponse) {
            Glide.with(context)
                .load(image.thumbnailUrl)
                .into(binding.ivPerson)
            binding.tvSitename.text = image.displaySitename
            binding.tvDatetime.text = simpleDateFormat.format(image.datetime)
            currentImage = image

            if(context is ActivityDataListener) {
                listener = context
            }

            itemView.setOnClickListener {
                currentImage.let {
                    if (ivHeart.visibility == View.VISIBLE) {
                        ivHeart.visibility = View.INVISIBLE
                        it?.status = false
                        if (it != null) {
                            listener?.onDataReceived(it) // 나중에 position으로 바꿔야 할 것 같긴 한데..
                        }
                    } else if (ivHeart.visibility == View.INVISIBLE) {
                        ivHeart.visibility = View.VISIBLE
                        it?.status = true
                        if (it != null) {
                            listener?.onDataReceived(it)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(data[position])
    }
}