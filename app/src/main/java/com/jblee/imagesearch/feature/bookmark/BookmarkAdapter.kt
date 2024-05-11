package com.jblee.imagesearch.feature.bookmark

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
 * 북마크 항목을 표시하기 위한 어댑터 클래스입니다.
 */
class BookmarkAdapter(var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 북마크된 아이템들을 저장하는 리스트
    var items = mutableListOf<SearchItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 이미지 로딩 라이브러리(Glide)를 사용해 썸네일 이미지를 로드한다.
        Glide.with(mContext)
            .load(items[position].url)
            .into((holder as ItemViewHolder).iv_thum_image)

        holder.tv_title.text = items[position].title
        holder.iv_like.visibility = View.GONE // '좋아요' 아이콘을 숨김
        holder.tv_datetime.text =
            getDateFromTimestampWithFormat(
                items[position].dateTime,
                "yyyy-MM-dd'T'HH:mm:ss.SSS+09:00",
                "yyyy-MM-dd HH:mm:ss"
            )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * RecyclerView의 각 항목을 표현하는 ViewHolder 클래스입니다.
     */
    // 어 layout 한개로 쓰는구나.. 난 두개썼는데! 리소스를 덜 낭비할 수 있겠다.
    inner class ItemViewHolder(binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) { // inner class 를 class 로 바꾸려 했는데 (스탠다드반 자료) 오류가 나네
        var iv_thum_image: ImageView = binding.ivThumImage
        var iv_like: ImageView = binding.ivLike
        var tv_title: TextView = binding.tvTitle
        var tv_datetime: TextView = binding.tvDatetime
        var cl_item: ConstraintLayout = binding.clThumbItem

        init { // init은 왜 쓰는거지? 생성자였나? 기억이 안난다..
            // 북마크 페이지에서는 '좋아요' 아이콘을 숨긴다.
            iv_like.visibility = View.GONE // 아! 해당 코드만 쓰면 굳이 레이아웃을 한개 더 만들 필요가 없겠구나

            // 아이템 클릭 리스너 설정 → 뷰홀더에서 처리하는구나
            cl_item.setOnClickListener {
                val position = adapterPosition // 내부 함수 사용! onBindViewHolder에서 position 안가져와도 되구나
                if (position != RecyclerView.NO_POSITION) {
                    // 보관함에서 데이터 삭제 후 갱신
                    (mContext as MainActivity).removeLikedItem(items[position])
                    items.removeAt(position)
                    notifyItemRemoved(position) //
                }
            }
        }
    }

}
