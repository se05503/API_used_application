# hw3_imageSearch_A_type

패키지 구조 확인하기

A type은 다시 이미지 검색으로 돌아왔을 때 기존 검색 화면을 보여주게 하진 않음

activity_main.xml : bottom navigation, viewpager 사용 가능

search_item.xml : ImageView에 maxHeight와 minHeight를 줌

작업 순서 : MainActivity -> 2개의 Fragment -> adapter -> API

remote data 를 가져오기 위해 필요한 3가지 구현 (순서도 중요)
순서 1) model(ImageModel, Response data class) : json을 GSON을 통해 파싱한 값을 가져오기 위해 API 문서의 응답(response) 값을 data class로 만듬
순서 2) interface(Request) : respnse data를 받기 위해 헤더, 쿼리로 요청을 함. 이때, base url 의 나머지 부분이 필요하다. 
순서 3) retrofit client : retrofit 객체를 만들고 2번에서 만든 인터페이스를 넣는다. 이때, base url을 사용한다.

(좋아요 클릭 데이터 반영: SearchFragment -> BookmarkFragment)
방식은 여러가지가 있다 
- 가장 많이 쓰는 방식: ViewModel -> B type
- interface
- 공유 저장소 만들기 

- SearchFragment에서 하트 누른게 BookmarkFragment에 반영되는 방식 
→ SearchFragment -> MainActivity -> BookmarkFragment (영상 방식) 
- BookmarkAdapter에서 

느낀점
- 생각보다 내부 자체적으로 정의된 함수를 많이 사용하신다. 사실 직접 정의하는 것보단 내부 함수를 쓰는게 더 편할 것 같다. 나도 내부함수를 이해하고 직접 써보는 연습을 해야할 것 같다.
- 아직 해당 코드에서도 버그가 있다. 하트가 반영이 안된다. 
- 특히 파일을 정의하는 '순서'를 아려주신 점이 좋았다.