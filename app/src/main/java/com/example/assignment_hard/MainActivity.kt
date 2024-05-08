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
    val selectedImageList = mutableListOf<DocumentResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFragment(SearchFragment()) // fragment 방식을 replace 와 add 비교해서 다음엔 add 적용해보기

        binding.apply {
            btnSearch.setOnClickListener {
                setFragment(SearchFragment())
            }
            btnStore.setOnClickListener {
                setFragment(StoreFragment())
            }
        }
    }

    /*
    val fragmentManger = supportFragmentManager
        val contactListFragment = ContactListFragment()
        val transaction = fragmentManger.beginTransaction()
        transaction.add(R.id.frameLayout, contactListFragment).commitAllowingStateLoss()
     */

    private fun setFragment(fragment:Fragment) {
        supportFragmentManager.commit {
            replace(R.id.frameLayout,fragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

    override fun onDataReceived(data: DocumentResponse) {
        Log.d("data",data.toString())
        selectedImageList.add(data)
    }
}