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
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            adapter.items.addAll(items)
            adapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
            loading = !isLoading
        }

        sharedViewModel.deletedItemUrls.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { urls ->
                urls.forEach { url ->
                    Log.d("SearchFragment", "#jblee sharedViewModel.deletedItemUrl url = $url")
                    val targetItem = adapter.items.find { it.url == url }

                    targetItem?.let {
                        it.isLike = false
                        val itemIndex = adapter.items.indexOf(it)
                        adapter.notifyItemChanged(itemIndex)
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
        binding.fbTop.visibility = View.GONE

        adapter = SearchAdapter(mContext)

        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.addOnScrollListener(onScrollListener)
        binding.rvSearchResult.itemAnimator = null
        binding.etSearch.setText("")
        binding.pbSearch.visibility = View.GONE
    }

    // 리스너 설정 함수
    private fun setupListeners() {
        binding.fbTop.setOnClickListener { v: View? ->
            binding.rvSearchResult.smoothScrollToPosition(0)
        }

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.tvSearch.setOnClickListener {
            if (binding.etSearch.text.toString() == "") {
                Toast.makeText(mContext, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                adapter.clearItem()
                lastQuery = binding.etSearch.text.toString()
                loading = false
                viewModel.doSearch(lastQuery, viewModel.curPageCnt)
            }
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
                    viewModel.doSearch(lastQuery, viewModel.curPageCnt)
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
