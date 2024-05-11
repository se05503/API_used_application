package com.jblee.imagesearch.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jblee.imagesearch.data.api.Retrofit_interface

/**
 * SearchViewModelFactory 클래스는 ViewModelProvider.Factory 인터페이스를 구현하는 사용자 정의 ViewModel 팩토리.
 * 이 팩토리는 SearchViewModel을 생성할 때 필요한 의존성을 주입하는 역할
 */

//Retrofit_interface 타입의 apiService를 매개변수로 받고, apiService는 나중에 SearchViewModel을 생성할 때 주입
class SearchViewModelFactory(private val apiService: Retrofit_interface) : ViewModelProvider.Factory {
    // create 함수는 ViewModelProvider.Factory 인터페이스에서 오버라이드된 함수로 ViewModel 객체를 반환합니다.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(apiService) as T
    }
}

/**
 * 제네릭 파라미터 T는 반환될 ViewModel의 타입을 나타냄
 * 함수 내에서는 SearchViewModel의 인스턴스를 생성하고, 필요한 apiService를 주입
 * 이후 생성된 SearchViewModel 인스턴스를 T 타입으로 캐스팅하여 반환
 */