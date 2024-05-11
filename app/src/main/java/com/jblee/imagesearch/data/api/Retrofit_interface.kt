package com.jblee.imagesearch.data.api

import com.jblee.imagesearch.data.model.ImageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Retrofit_interface는 Retrofit2 라이브러리를 사용하여 API 엔드포인트와 상호작용하는 인터페이스입니다.
 * 이 인터페이스는 Kakao 검색 API에 대한 endpoint를 정의하며, 여기에서는 이미지 검색 endpoint를 사용합니다.
 */
interface Retrofit_interface {

    /**
     * API endpoint for image search.
     *
     * @param apiKey Authorization key for the API.
     * @param query Search keyword.
     * @param sort Sorting order (e.g. recency).
     * @param page Page number for pagination.
     * @param size Number of results per page.
     *
     * @return A Call object of type ImageModel.
     */
    @GET("v2/search/image") // URL 에서 base url 의 나머지 부분을 넣어줌
    fun image_search( // response data를 얻기 위한 요청 파라미터(헤더, 쿼리)를 넣어줌
        @Header("Authorization") apiKey: String?,
        @Query("query") query: String?,
        @Query("sort") sort: String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ImageModel?>? // 우와 Call 이라는 내부 함수를 씀, 아하! 해당 요청 변수들을 통해 response data를 call 한다는 의미로 쉽게 이해할 수 있네!
}

/**
 * 주의: @Header, @Query 등의 애너테이션은 Retrofit2에서 API 호출의 특정 부분을 표시하는 데 사용됩니다.
 * 예를 들어, @Query는 URL 쿼리 파라미터를 표시하며, @Header는 HTTP 헤더 값을 나타냅니다.
 */