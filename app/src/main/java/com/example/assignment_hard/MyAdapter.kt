package com.example.assignment_hard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.ItemLayoutBinding

/*
첫번째 오류 해결과정
1. Glide의 with에 context를 받아와하는데 fragment로 잘못 설정했다. → adapter에 변수 하나 더 추가(fragment로부터 context 받아오기)
2. Fragment 의 생명주기 이해 : OnCreate가 아닌 onViewCreated에 설정해야 한다.
 */
class MyAdapter(private val data: MutableList<DocumentResponse>, val context: Context):RecyclerView.Adapter<MyAdapter.ImageViewHolder>() {

    class ImageViewHolder(private var binding:ItemLayoutBinding, val context: Context):RecyclerView.ViewHolder(binding.root) {
        private var currentImage : DocumentResponse? = null

        fun bind(image:DocumentResponse) {
            currentImage = image
            Glide.with(context)
                .load(image.thumbnailUrl)
                .into(binding.ivPerson)
            binding.tvSitename.text = image.displaySitename
            binding.tvDatetime.text = image.datetime.toString() // 해당 코드 변경하기
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(binding,context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(data[position])
    }
}