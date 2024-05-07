package com.example.assignment_hard.data

import com.google.gson.annotations.SerializedName
import java.util.Date

// API로부터 받은 응답을 저장할 클래스 정의

// 전체 API 응답
data class ImageResponse(
    @SerializedName("meta") val meta: MetaResponse,
    @SerializedName("documents") val documents: MutableList<DocumentResponse>
)

// Meta data 정보
data class MetaResponse(
    @SerializedName("total_count") val totalCount : Int,
    @SerializedName("pageable_count") val pageableCount : Int,
    @SerializedName("is_end") val isEnd : Boolean
)

// 각 이미지에 대한 상세정보 → 가져오고 싶은 부분만 가져오면 된다.
data class DocumentResponse(
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("display_sitename") val displaySitename: String,
    @SerializedName("datetime") val datetime: Date
)