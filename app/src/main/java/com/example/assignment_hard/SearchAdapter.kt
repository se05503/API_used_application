package com.example.assignment_hard

import android.content.Context
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

class SearchAdapter(
    private val data: MutableList<DocumentResponse>,
    private val context: Context
) : RecyclerView.Adapter<SearchAdapter.ImageViewHolder>() {

    private lateinit var imageViewHolder: ImageViewHolder

    class ImageViewHolder(private var binding: ItemLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private val ivHeart = binding.ivHeart
        private var listener: ActivityDataListener? = null
        private var heartItemList: ArrayList<DocumentResponse>?=null

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

            if (context is ActivityDataListener) {
                listener = context
            }

            // 검색 눌렀을 때 실행
            if(image.status == false) {
                Log.d("##","##")
                ivHeart.visibility = View.INVISIBLE
            } else if(image.status == true) {
                ivHeart.visibility = View.VISIBLE // 검색 눌렀을 때 하트가 사라지는 현상 해결 코드
            }

            itemView.setOnClickListener {
                Log.d("realproblem",currentImage.toString())
                currentImage.let {
                    if (it?.status == true) {
                        ivHeart.visibility = View.INVISIBLE
                        it?.status = false
                        if (it != null) {
                            heartItemList?.remove(it)
                            listener?.onDataReceived(it) // 나중에 position으로 바꿔야 할 것 같긴 한데..
                        }
                    } else if (it?.status == false) {
                        ivHeart.visibility = View.VISIBLE
                        it?.status = true
                        Log.d("problem",currentImage.toString())
                        if (it != null) {
                            heartItemList?.add(it)
                            listener?.onDataReceived(it)
                        }
                    }
                }
            }
        }
    }

    // 불리는 시점 : 보관함에서 삭제했을 때
    fun getUpdatedData(updateItemList: ArrayList<DocumentResponse>) {
        // heartItemList의 값을 빼버리는 것보단 heart Status를 false로 바꾸는게 낫겠지?
//        for (element in imageViewHolder.heartItemList) {
//            if (!updateItemList.contains(element)) element.status = false
//        }
//        notifyDataSetChanged()
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