package com.example.assignment_hard

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ActivityDataListener {

    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    val selectedImageList = arrayListOf<DocumentResponse>()

    private val searchFragment = SearchFragment() // 내가 아직 기존 객체 쓰는거랑 새로운 객체 쓰는거 잘 구별을 못하는 것 같음. 일단, 새로운 객체 생성하지 않기 위해 변수 생성
//    val storeFragment = StoreFragment.newInstance(selectedImageList) // 얘는 아마 계속 생성해야 하지 않을까?
    private val storeFragment = StoreFragment.getFragment().newInstance(selectedImageList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity","onCreate")
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.frameLayout,searchFragment) // 어짜피 onCreate는 한번만 실행되니까 add 를 쓰자!
            .commit()

        binding.apply {
            btnSearch.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frameLayout,searchFragment) // searchFragment를 hide 하고 show를 하기 때문에 프래그먼트가 보여도 생명주기가 움직이지 않음?
                    .commit()
            }

            btnStore.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frameLayout,storeFragment)
                    .commit()
            }
        }
    }


    override fun onDataReceived(data: DocumentResponse) {
        Log.d("data",data.toString())
        if(!selectedImageList.contains(data)) selectedImageList.add(data) // 아이템이 중복으로 리스트에 들어가는 것을 막음
        Log.d("list", selectedImageList.toString())
    }
}