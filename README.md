# hw3_imageSearch_B_type

## 프로그램 설명
이 프로젝트는 이미지 검색 및 북마크 기능을 제공하는 애플리케이션입니다. 사용자는 이미지를 검색하고 원하는 이미지를 북마크에 추가할 수 있습니다.

## 폴더 및 파일 구조
- `data/`: 애플리케이션의 데이터 로직과 API 호출을 처리합니다.
    - `api/` → request params
        - `Retrofit_interface`: Retrofit을 통한 API 호출 인터페이스 정의
    - `model/` → response data
        - `ImageModel`: 이미지 정보를 저장하는 데이터 모델
        - `VideoModel`: 비디오 정보를 저장하는 데이터 모델
    - `Retrofit_client.kt`: Retrofit 인스턴스 및 설정 관련 로직 → 여기서 api interface를 호출함

- `ui/`: 애플리케이션의 UI를 담당하는 Fragment 및 Adapter가 위치합니다. → 응? Activity도 넣는 줄 알았는데 밖으로 뺐구나. Adapter도 ui에 들어가는구나.
    - `bookmark/`
        - `BookmarkAdapter`: 북마크 화면에서 사용하는 리사이클러뷰 어댑터
        - `BookmarkFragment`: 북마크 화면 UI 및 로직 처리
        - 'BookmarkViewModel' : ... Feature 별로 묶을 경우 추가 
    - `search/`
        - `SearchAdapter`: 검색 화면에서 사용하는 리사이클러뷰 어댑터
        - `SearchFragment`: 이미지 검색 화면 UI 및 로직 처리

- `utils/`
    - `Utils`: 프로젝트 전반에 사용되는 유틸리티 함수 모음

- `viewmodel/`: MVVM의 ViewModel을 포함하며, UI 로직과 데이터의 중개 역할을 합니다. LiveData를 사용하여 UI의 상태 및 데이터 변경을 관찰합니다. 비즈니스 로직이 구현되어 있습니다.
    - `bookmark/`
        - `BookmarkViewModel`: 북마크 화면의 데이터 및 로직 처리를 담당
    - `search/`
        - `SearchViewModel`: 검색 화면의 데이터 및 로직 처리를 담당
        - `SearchViewModelFactory`: SearchViewModel 생성을 위한 Factory 클래스
    - `SharedViewModel.kt`: 여러 Fragment 간에 공유되는 데이터를 관리 → 중요
    - 아.. ViewModel 을 총 3개 만드는구나..

- `Constants`: 프로젝트 전반에 사용되는 상수 값 모음
- `MainActivity`: 애플리케이션의 메인 엑티비티

## MVVM 패턴
이 프로젝트는 MVVM(Model-View-ViewModel) 디자인 패턴이 적용되었습니다. View와 Business Logic이 분리되어 있어 **유지 보수**와 확장성이 높아집니다.

## 강의 영상 메모
- 동영상 추가
- MVVM 중 ViewModel의 LiveData 기능 사용
- 큰 틀: data(Model), UI(View), ViewModel → Feature 로 묶어도 된다. → 방식은 개인 성향, 프로젝트에 따라 다르다. (정답은 없다)
- bottom navigation view 사용
- text 를 사용하는 경우 string xml에다가 저장하는 연습을 하자.
- 팀원할 때 코드 설명할 때 기준을 모르겠으면 "기능" 위주로 설명하기
- 무한 스크롤 기능
- SearchFragment 에서 좋아요 한 데이터를 preference에 저장해야 한다.
- 역직렬화 : json 데이터 → 객체 (서버로부터 데이터를 가져올 때 보통 사용) ↔ 직렬화 : 객체 → json (sharedpreference에 객체 값을 넣기 위해 사용)
- MainActivity.xml 에서 navigation UI 부분 코드 공부하기
- Util 의 getDataFromTimestampWithFormat 함수 코드 이해하고 내 문서용 프로젝트에 저장하기
- 코드 읽는 순서 
1) SearchFragment(network 데이터 받아와서 뿌려주기) 1-1) SearchViewModel 정의 확인 → 1-2) SearchFragment의 observe 부분 확인 ※ api interface 로부터 쿼리 날려서 remote data 받아오는건 fragment 가 아닌 viewmodel 에 메서드 내에 정의하기
2) BookmarkFragment(SearchFragment 에서 좋아요 클릭한 아이템을 BookmarkFragment 에 반영하기) → 클릭 아이템을 preference 에 저장 ↔ 보관함에서 preference 데이터 불러오기 (매개: preference)
3) BookmarkFragment 에서의 데이터 삭제 및 SearchFragment 에서 좋아요 해제 
   3-1) BookmarkAdapter 에서 내부 인터페이스 생성 및 onClick 이벤트에 인터페이스 사용
   3-2) BookmarkFragment 에서 인터페이스 구현
   3-3) sharedViewModel 

