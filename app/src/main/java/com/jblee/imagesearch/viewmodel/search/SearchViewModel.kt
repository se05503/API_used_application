package com.jblee.imagesearch.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jblee.imagesearch.Constants
import com.jblee.imagesearch.data.api.Retrofit_interface
import com.jblee.imagesearch.data.model.ImageModel
import com.jblee.imagesearch.data.model.VideoModel
import com.jblee.imagesearch.model.SearchItemModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class SearchViewModel(private val apiService: Retrofit_interface) : ViewModel() {

    // 검색 결과에 대한 LiveData 선언
    private val _searchResults = MutableLiveData<List<SearchItemModel>>()
    val searchResults: LiveData<List<SearchItemModel>> get() = _searchResults

    // 로딩 상태에 대한 LiveData 선언
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 페이지, 결과 아이템 등의 변수 선언
    var curPageCnt: Int = 1
    var resItems: ArrayList<SearchItemModel> = ArrayList()
    var maxImagePage: Int = 1
    var maxVideoPage: Int = 1

    var isImageSearchFinished = false
    var isVideoSearchFinished = false

    // 검색을 실행하는 메서드
    fun doSearch(query: String, page: Int) {
        Log.d("SearchViewModel", "#jblee doSearch query=$query, page=$page, maxImagePage=$maxImagePage, maxVideoPage=$maxVideoPage")
        resItems.clear()
        _isLoading.value = true

        isImageSearchFinished = false
        isVideoSearchFinished = false

        // 페이지 범위 내에서 이미지와 비디오 검색 수행
        if (page <= maxImagePage) {
            fetchImageResults(query, page)
        }

        if (page <= maxVideoPage) {
            fetchVideoResults(query, page)
        }
    }

    // 이미지 검색 결과를 가져오는 메서드
    private fun fetchImageResults(query: String, page: Int) {
        apiService.image_search(Constants.AUTH_HEADER, query, "recency", page, 40)
            ?.enqueue(object : Callback<ImageModel?> {
                override fun onResponse(call: Call<ImageModel?>, response: Response<ImageModel?>) {
                    response.body()?.meta?.let { meta ->
                        if (meta.totalCount > 0) {
                            for (document in response.body()!!.documents) {
                                val title = document.displaySitename
                                val datetime = document.datetime
                                val url = document.thumbnailUrl
                                resItems.add(SearchItemModel(Constants.SEARCH_TYPE_IMAGE, title, datetime, url))
                            }
                            maxImagePage = meta.pageableCount
                        }
                    }
                    Log.d("SearchViewModel", "#jblee fetchImageResults maxImagePage=$maxImagePage")
                    isImageSearchFinished = true
                    checkSearchCompletion()
                }

                override fun onFailure(call: Call<ImageModel?>, t: Throwable) {
                    Log.e("#jblee", "onFailure: ${t.message}")
                }
            })
    }

    // 비디오 검색 결과를 가져오는 메서드
    private fun fetchVideoResults(query: String, page: Int) {
        apiService.video_search(Constants.AUTH_HEADER, query, "recency", page, 15)
            ?.enqueue(object : Callback<VideoModel?> {
                override fun onResponse(call: Call<VideoModel?>, response: Response<VideoModel?>) {
                    response.body()?.meta?.let { meta ->
                        if (meta.totalCount > 0) {
                            for (document in response.body()!!.documents) {
                                val title = document.title
                                val datetime = document.datetime
                                val url = document.thumbnail
                                resItems.add(SearchItemModel(Constants.SEARCH_TYPE_VIDEO, title, datetime, url))
                            }
                            maxVideoPage = meta.pageableCount
                        }
                    }
                    isVideoSearchFinished = true
                    checkSearchCompletion()
                }

                override fun onFailure(call: Call<VideoModel?>, t: Throwable) {
                    Log.e("##jblee", "onFailure: ${t.message}")
                }
            })
    }

    // 이미지와 비디오 검색이 모두 완료되었는지 확인하는 메서드
    private fun checkSearchCompletion() {
        if (isImageSearchFinished && isVideoSearchFinished) {
            searchResult()
        }
    }

    // 검색 결과를 LiveData에 설정하는 메서드
    private fun searchResult() {
        // 날짜 별로 정렬
        Collections.sort(resItems) { baseClass, t1 -> t1.dateTime.compareTo(baseClass.dateTime) }

        // 검색 결과를 LiveData에 설정
        _searchResults.value = resItems

        // 로딩 상태 업데이트
        _isLoading.value = false
    }
}
