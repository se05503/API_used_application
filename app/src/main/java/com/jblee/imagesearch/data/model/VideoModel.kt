package com.jblee.imagesearch.data.model

import com.google.gson.annotations.SerializedName

/**
 * 비디오 검색 응답을 위한 모델 클래스.
 */
data class VideoModel(
    @SerializedName("documents")
    var documents: ArrayList<Documents>,

    @SerializedName("meta")
    var meta: Meta
) {
    /**
     * 비디오 검색 응답에서 단일 문서 혹은 결과를 나타내는 클래스.
     */
    data class Documents(
        @SerializedName("title")
        var title: String,

        @SerializedName("url")
        var url: String,

        @SerializedName("datetime")
        var datetime: String,

        @SerializedName("play_time")
        var playTime: Int,

        @SerializedName("thumbnail")
        var thumbnail: String,

        @SerializedName("author")
        var author: String
    )

    /**
     * 비디오 검색 응답에 대한 메타 정보를 나타내는 클래스.
     */
    data class Meta(
        @SerializedName("is_end")
        var isEnd: Boolean,

        @SerializedName("pageable_count")
        var pageableCount: Int,

        @SerializedName("total_count")
        var totalCount: Int
    )
}
