package com.jblee.imagesearch.model

/**
 * 검색된 항목을 표현하는 데이터 모델.
 *
 * @param type 검색된 항목의 타입 (이미지, 비디오 등).
 * @param title 검색된 항목의 제목.
 * @param dateTime 검색된 항목의 날짜 및 시간 정보.
 * @param url 검색된 항목의 URL.
 */
class SearchItemModel(var type: Int, var title: String, var dateTime: String, var url: String) { // A type 에서는 model을 data class로 정의했는데, 여기서는 class로 정의했네 둘이 무슨 차이일까?

    // 항목이 '좋아요' 상태인지 나타내는 변수. 기본값은 false.
    var isLike = false
}
