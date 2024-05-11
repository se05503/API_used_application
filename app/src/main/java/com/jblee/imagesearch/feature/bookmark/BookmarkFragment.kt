package com.jblee.imagesearch.feature.bookmark

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jblee.imagesearch.MainActivity
import com.jblee.imagesearch.databinding.FragmentBookmarkBinding
import com.jblee.imagesearch.model.SearchItemModel

/**
 * 사용자의 북마크를 표시하는 프래그먼트입니다.
 */
class BookmarkFragment : Fragment() {

    private lateinit var mContext: Context

    // 바인딩 객체를 null 허용으로 설정 (프래그먼트의 뷰가 파괴될 때 null 처리하기 위함) → lateinit으로 하면 문제점: 반드시 초기화해야해서 null 처리가 힘듬(?)
    private var binding: FragmentBookmarkBinding? = null // 초기화되지 않은 상태에서 사용 가능
    private lateinit var adapter: BookmarkAdapter // 선언된 직후에는 초기화하지 않으나 이후 반드시 초기화해야함 (그렇지 않으면 런타임 오류가 발생함)

    // 사용자의 좋아요를 받은 항목을 저장하는 리스트
    private var likedItems: List<SearchItemModel> = listOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    // 아.. 프래그먼트에서는 코드를 보통 onCreateView에 넣는구나
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // MainActivity로부터 좋아요 받은 항목을 가져옴
        val mainActivity = activity as MainActivity // 정의하는 방식을 잘 살펴보자.
        likedItems = mainActivity.likedItems // 굳이 인터페이스 쓰지 않고도 이런 식으로 데이터 전달을 하면 되는구나..

        Log.d("BookmarkFragment", "#jblee likedItems size = ${likedItems.size}")

        // 어댑터 설정
        adapter = BookmarkAdapter(mContext).apply { // 객체 자신 반환, apply 함수 앞 객체도 중괄호 내용으로 변경됨
            items = likedItems.toMutableList() // 오 해당 표현 되게 괜찮다! fragment에서 adapter 로 이런 방식으로 데이터를 전달하는구나! 근데 여기서는 처음부터 ArrayList를 쓰지 않고, 정적인 List로 선언했다가 MutableList로 바꿔주네
        }

        // 바인딩 및 RecyclerView 설정
        binding = FragmentBookmarkBinding.inflate(inflater, container, false).apply { // apply 함수: 객체 자신 반환, 자신도 중괄호 내용으로 바뀜
            rvBookmark.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvBookmark.adapter = adapter
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수를 방지하기 위해 뷰가 파괴될 때 바인딩 객체를 null로 설정 → 오 첨 알게된 내용인데! 한수 배워갑니다.
        binding = null
    }
}
