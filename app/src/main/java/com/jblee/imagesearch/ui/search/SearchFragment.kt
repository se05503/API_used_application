package com.jblee.imagesearch.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jblee.imagesearch.databinding.FragmentSearchBinding
import com.jblee.imagesearch.data.retrofit_client
import com.jblee.imagesearch.viewmodel.search.SearchViewModel
import com.jblee.imagesearch.viewmodel.search.SearchViewModelFactory
import com.jblee.imagesearch.viewmodel.search.SharedViewModel

class SearchFragment : Fragment() {

    // 뷰 바인딩 및 변수 초기화
    private lateinit var binding: FragmentSearchBinding
    private lateinit var mContext: Context
    private lateinit var adapter: SearchAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager
    val apiServiceInstance = retrofit_client.apiService

    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(apiServiceInstance) }
//    private val sharedViewModel: SharedViewModel by activityViewModels()
    val sharedViewModel by activityViewModels<SharedViewModel>()

    // 검색 상태 변수들
    private var lastQuery = ""
    private var loading = true
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    // Fragment가 Context에 첨부될 때 Context 참조 가져오기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    // 프래그먼트 뷰 생성 및 초기화
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupViews(inflater, container) // 뷰 설정 메서드 호출
        setupListeners()                // 리스너 설정 메서드 호출

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()              // ViewModel 관찰 설정

    }

    // ViewModel에서 데이터 변화를 관찰하는 함수
    private fun observeViewModel() {
        // viewModel 에서 livedata 내용이 바뀌면 실행되는건가?
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            adapter.items.addAll(items)
            adapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbSearch.visibility = if (isLoading) View.VISIBLE else View.GONE // 로딩 중이면 프로그래스바가 보이고, 로딩이 끝나면 프로그래스바를 안보이게 한다.
            loading = !isLoading
        }

        sharedViewModel.deletedItemUrls.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { urls ->
                urls.forEach { url ->
                    Log.d("SearchFragment", "#jblee sharedViewModel.deletedItemUrl url = $url")
                    val targetItem = adapter.items.find { it.url == url }

                    // targetItem : SearchFragment에서 좋아요 반영을 취소해야할 아이템
                    targetItem?.let {
                        it.isLike = false
                        val itemIndex = adapter.items.indexOf(it)
                        adapter.notifyItemChanged(itemIndex) // 취소 대상이 되는 아이템의 포지션 값에 대한 데이터 갱신을 한다.
                    }
                    // 처리 후 목록을 비워줍니다.
                    sharedViewModel.clearDeletedItemUrls()
                }
            }

        }



    }

    // 뷰 초기 설정 함수
    private fun setupViews(inflater: LayoutInflater, container: ViewGroup?) {
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.rvSearchResult.layoutManager = gridmanager
        binding.fbTop.visibility = View.GONE // 초기 상태에서는 스크롤을 아래로 하지 않기 때문에 플로팅 버튼이 보이지 않음

        adapter = SearchAdapter(mContext)

        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.addOnScrollListener(onScrollListener) // ?
        binding.rvSearchResult.itemAnimator = null
        binding.etSearch.setText("")
        binding.pbSearch.visibility = View.GONE // 프로그래스바
    }

    // 리스너 설정 함수
    private fun setupListeners() {
        binding.fbTop.setOnClickListener { v: View? ->
            binding.rvSearchResult.smoothScrollToPosition(0)  // 와 내부함수가 있구나.. 대박
        }

        binding.tvSearch.setOnClickListener {
            if (binding.etSearch.text.toString() == "") {
                Toast.makeText(mContext, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 사용자가 검색어를 입력한 경우
                adapter.clearItem() // 중요한 코드! 안그러면 다른 검색어를 입력했을 때 리사이클러뷰가 갱신이 안된다. (이전 검색 데이터 초기화)
                lastQuery = binding.etSearch.text.toString()
                loading = false
                viewModel.doSearch(lastQuery, viewModel.curPageCnt)
            }

            // 검색 했을 때 키보드 내리는 코드
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }
    }

    // 리사이클러뷰 스크롤 리스너: 사용자가 스크롤할 때 다음 페이지의 데이터를 로드하는 역할
    private var onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                visibleItemCount = gridmanager.childCount
                totalItemCount = gridmanager.itemCount

                val firstVisibleItems = gridmanager.findFirstVisibleItemPositions(null)
                if (firstVisibleItems.isNotEmpty()) {
                    pastVisibleItems = firstVisibleItems[0]
                }

                if (loading && visibleItemCount + pastVisibleItems >= totalItemCount) {
                    loading = false
                    viewModel.curPageCnt += 1
                    viewModel.doSearch(lastQuery, viewModel.curPageCnt) // 페이지가 넘어갈때마다 doSearch가 불림
                }

                if (dy > 0 && binding.fbTop.visibility == View.VISIBLE) {
                    binding.fbTop.hide()
                } else if (dy < 0 && binding.fbTop.visibility != View.VISIBLE) {
                    binding.fbTop.show()
                }

                if (!recyclerView.canScrollVertically(-1)) {
                    binding.fbTop.hide()
                }
            }
        }
}
