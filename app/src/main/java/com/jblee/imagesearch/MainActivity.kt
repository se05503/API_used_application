package com.jblee.imagesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.jblee.imagesearch.databinding.ActivityMainBinding

/**
 * MainActivity는 앱의 주 활동이며, 여기서 사용자의 주 탐색 인터페이스인 BottomNavigationView와
 * Navigation Component를 설정하고 연결합니다.
 */
class MainActivity : AppCompatActivity() {

    // ActivityMainBinding은 MainActivity의 레이아웃 바인딩 객체입니다.
    // 이를 통해 레이아웃의 뷰에 직접 접근할 수 있습니다.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityMainBinding 객체를 초기화합니다. 이 객체는 layoutInflater를 사용하여
        // activity_main.xml 레이아웃을 인플레이트합니다.
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 초기화된 바인딩 객체의 root 뷰를 활동의 콘텐츠 뷰로 설정합니다.
        setContentView(binding.root)

        // R.id.nav_host_fragment_activity_main ID를 가진 NavHostFragment의 NavController를 가져옵니다.
        // NavController는 Navigation Component에서 프래그먼트 간의 탐색을 관리합니다.
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // BottomNavigationView를 NavController와 연결합니다.
        // 이를 통해 탐색 메뉴 항목이 클릭될 때 적절한 프래그먼트로 자동 탐색됩니다.
        NavigationUI.setupWithNavController(binding.navView, navController)
    }
}
