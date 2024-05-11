package com.jblee.imagesearch

/**
 * 앱 전체에서 사용되는 상수들을 저장하는 객체입니다.
 */
object Constants {

    // Kakao Image Search API의 기본 URL입니다.
    const val BASE_URL = "https://dapi.kakao.com" // 아! base url 도 상수 파일에서 처리하는구나...!

    // Kakao API를 사용하기 위한 인증 헤더입니다.
    const val AUTH_HEADER = "KakaoAK 7581f80aa0c7a862f5cd0af5865d9511"

    // 앱의 Shared Preferences 파일 이름입니다.
    const val PREFS_NAME = "com.jblee.imagesearch.prefs"

    // 마지막 검색어를 저장하기 위한 키 값입니다.
    const val PREF_KEY = "IMAGE_SEARCH_PREF"
}
