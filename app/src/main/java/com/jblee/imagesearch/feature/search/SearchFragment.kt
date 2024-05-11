package com.jblee.imagesearch.feature.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jblee.imagesearch.Constants
import com.jblee.imagesearch.data.model.ImageModel
import com.jblee.imagesearch.databinding.FragmentSearchBinding
import com.jblee.imagesearch.data.retrofit_client.apiService
import com.jblee.imagesearch.model.SearchItemModel
import com.jblee.imagesearch.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 사용자에게 이미지 검색 기능을 제공하는 Fragment 클래스입니다.
 */
class SearchFragment : Fragment() {

    // 오 lateinit 형식을 사용하는구나! 난 이상하게 lateinit 쓰면 런타임때 초기화 안했다고 에러 떴는데..

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mContext: Context
    private lateinit var adapter: SearchAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager

    private var resItems: ArrayList<SearchItemModel> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context // 지연 변수 초기화?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // 아 이걸 fragment의 onCreateView에 넣는거구나.
        setupViews()   // 뷰 초기 설정
        setupListeners() // 리스너 설정
        // 아 .. 내부에는 코드를 안쓰고 다 method 로 외부로 빼는구나

        return binding.root
    }

    /**
     * 화면 뷰들의 초기 설정을 하는 메서드입니다.
     */
    private fun setupViews() {
        // RecyclerView 설정
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSearchResult.layoutManager = gridmanager

        adapter = SearchAdapter(mContext) // 오 adapter에 데이터 안넣네
        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.itemAnimator = null // 뭔가 데이터 변경을 가할 경우 recyclerView 주변 item 들이 깜빡이는 경우가 있다. 그런 버그를 해결하려면 itemAnimator를 null 처리 해야한다.

        // 최근 검색어를 가져와 EditText에 설정
        val lastSearch = Utils.getLastSearch(requireContext())
        binding.etSearch.setText(lastSearch)
    }

    /**
     * 화면에 있는 UI 요소들의 리스너 설정을 하는 메서드입니다.
     */
    private fun setupListeners() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // 키보드 숨기기
        binding.tvSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotEmpty()) { // null check
                // 마지막 검색어 저장 (preference)
                Utils.saveLastSearch(requireContext(), query)
                adapter.clearItem() // 해당 함수를 통해서 사용자로부터 받아오는 검색어에 대한 데이터를 다시 갱신할 수 있는 것 같다 (내가 맨 마지막에 해결하지 못한 트러블 슈팅에 대한 해답이 될 수 있을 것 같다)
                fetchImageResults(query)
            } else {
                Toast.makeText(mContext, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            // 키보드 숨기기
            imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }
    }

    /**
     * 입력한 검색어로 이미지를 검색하는 메서드입니다.
     */
    private fun fetchImageResults(query: String) {
        apiService.image_search(Constants.AUTH_HEADER, query, "recency", 1, 80) // 인터페이스 구현을 여기서 한다.
            ?.enqueue(object : Callback<ImageModel?> { // 비동기식 : 호출을 하고 처리가 다 되면 onResponse 메서드를 통해서 콜백을 부름, 요청을 하고 Callback을 기다린다
                override fun onResponse(call: Call<ImageModel?>, response: Response<ImageModel?>) { // 콜백이 오면 onResponse 메서드가 실행된다.
                    response.body()?.meta?.let { meta ->
                        if (meta.totalCount > 0) { // 검색 결과가 있는가?
                            response.body()!!.documents.forEach { document ->
                                val title = document.displaySitename // 여기서는 필요한 데이터만 가져오면 되는구나
                                val datetime = document.datetime
                                val url = document.thumbnailUrl
                                resItems.add(SearchItemModel(title, datetime, url)) // 검색 결과가 document 형태로 주르륵 쌓임
                            }
                        }
                    }
                    adapter.items = resItems // 이렇게 데이터를 연결하는구나
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ImageModel?>, t: Throwable) {
                    Log.e("#jblee", "onFailure: ${t.message}")
                }
            })
    }
}
