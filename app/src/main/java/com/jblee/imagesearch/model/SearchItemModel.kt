package com.jblee.imagesearch.model

/**
 * 검색 결과로 반환되는 각 이미지 아이템에 대한 모델 클래스입니다.
 *
 * @property title 이미지의 제목입니다.
 * @property dateTime 이미지가 생성 또는 업로드된 날짜와 시간입니다.
 * @property url 이미지의 URL 주소입니다.
 * @property isLike 사용자가 해당 이미지를 "좋아요" 했는지의 여부를 나타냅니다.
 */
data class SearchItemModel(
    var title: String,
    var dateTime: String,
    var url: String,
    var isLike: Boolean = false
)
