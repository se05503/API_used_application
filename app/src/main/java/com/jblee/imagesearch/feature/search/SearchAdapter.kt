package com.jblee.imagesearch.feature.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jblee.imagesearch.MainActivity
import com.jblee.imagesearch.utils.Utils.getDateFromTimestampWithFormat
import com.jblee.imagesearch.databinding.SearchItemBinding
import com.jblee.imagesearch.model.SearchItemModel

/**
 * 이미지 검색 결과를 표시하는 어댑터 클래스입니다.
 */

// Glide 를 쓰려면 context가 필요한데, 어댑터에서는 context를 생성할 수 없어서 프래그먼트에서 넘겨줘야 한다.
class SearchAdapter(private val mContext: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    var items = ArrayList<SearchItemModel>()

    /**
     * 아이템 목록을 초기화하는 메서드입니다.
     */
    fun clearItem() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        Glide.with(mContext)
            .load(currentItem.url)
            .into(holder.iv_thum_image)

        holder.iv_like.visibility = if (currentItem.isLike) View.VISIBLE else View.INVISIBLE
        holder.tv_title.text = currentItem.title
        holder.tv_datetime.text = getDateFromTimestampWithFormat(
            currentItem.dateTime,
            "yyyy-MM-dd'T'HH:mm:ss.SSS+09:00", // 우와 첨 본다! 내가 짠 코드보다 복잡하다. 하지만 그만큼 얻을 것도 많을 것이다!
            "yyyy-MM-dd HH:mm:ss"
        )
    }

    override fun getItemCount() = items.size

    inner class ItemViewHolder(binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var iv_thum_image: ImageView = binding.ivThumImage
        var iv_like: ImageView = binding.ivLike
        var tv_title: TextView = binding.tvTitle
        var tv_datetime: TextView = binding.tvDatetime
        var cl_thumb_item: ConstraintLayout = binding.clThumbItem

        init {
            iv_like.visibility = View.GONE
            iv_thum_image.setOnClickListener(this)
            cl_thumb_item.setOnClickListener(this)
        }

        /**
         * 각 항목 클릭 시 발생하는 이벤트를 처리하는 메서드입니다.
         */
        // 클릭 이벤트 처리를 ViewHolder에서 하는구나~
        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return // 와 item click 한 것에 대한 position을 onBindViewHolder에서만 가져올 수 있는줄 알았는데 이런 식으로 가져와서 ViewHolder에서 쓸 수 있구나!
            val item = items[position]

            item.isLike = !item.isLike

            if (item.isLike) { // 어댑터에서 메인액티비티로 데이터 변경사항을 전달하는 코드
                (mContext as MainActivity).addLikedItem(item)
            } else {
                (mContext as MainActivity).removeLikedItem(item)
            }

            notifyItemChanged(position)
        }
    }
}
