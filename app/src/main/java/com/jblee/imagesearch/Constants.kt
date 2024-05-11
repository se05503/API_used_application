package com.jblee.imagesearch

/**
 * Constants 객체는 앱 전체에서 공통적으로 사용되는 상수 값을 모아둔 싱글턴 객체입니다.
 * 이 객체를 사용함으로써 마법의 숫자나 문자열 대신 의미 있는 이름을 갖는 상수를 사용할 수 있습니다.
 * 이렇게 함으로써 코드의 가독성과 유지 보수성이 향상됩니다.
 */

object Constants {
    // 기본 API 엔드포인트. 여기서 모든 API 요청의 기본 URL로 사용됩니다.
    var BASE_URL = "https://dapi.kakao.com"

    // Kakao API를 사용하기 위한 인증 헤더입니다.
    var AUTH_HEADER = "KakaoAK 7581f80aa0c7a862f5cd0af5865d9511"

    // 이미지 검색을 나타내는 타입 코드입니다.
    var SEARCH_TYPE_IMAGE = 0

    // 비디오 검색을 나타내는 타입 코드입니다.
    var SEARCH_TYPE_VIDEO = 1

    // 이미지 "좋아요" 상태를 저장하기 위한 Shared Preferences 키입니다.
    var PREF_KEY = "IMAGE_LIKE_PREF"
}
