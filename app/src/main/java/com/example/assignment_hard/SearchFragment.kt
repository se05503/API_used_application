package com.example.assignment_hard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.FragmentSearchBinding
import com.example.assignment_hard.databinding.ItemLayoutBinding
import com.example.assignment_hard.network.RetrofitClient
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SearchFragment : Fragment() {

    // 업데이트 : notifysetchange

    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private var items = mutableListOf<DocumentResponse>()

    private val myAdapter: MyAdapter by lazy {
        MyAdapter(
            items,
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData() // onViewCreate() 위치랑 헷갈림 → 튜터님 말씀: 왠만한건 onViewCreated에 넣으면 된다! 그리고 loadData 메소드는 어디서나 불러쓸 수 있다.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), 2) // layoutManager 형태를 바꾸지 않을 경우 맨 앞에 넣어줘도 된다.
        binding.btnSearch.setOnClickListener {
            val searchResult = binding.etSearchName.text.toString() // 트러블 슈팅1) 오류 해결 코드 (밖으로 빼면 안됨)
            saveData(searchResult) // shared preference
            communicationNetWork(setUpImageParameter(searchResult))
        }
    }

    // activity를 인자로 하여 keyboard가 있는 Token 값을 찾아 내려주는 형식
    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.applicationWindowToken, 0)
    }

    // sharedPreference
    private fun saveData(search: String) {
        val sharedPref = requireActivity().getSharedPreferences("search_data", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putString("last_search", search)
        edit.apply()
    }

    private fun loadData() {
        val sharedPref = requireActivity().getSharedPreferences("search_data", Context.MODE_PRIVATE)
        binding.etSearchName.setText(sharedPref.getString("last_search", ""))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setUpImageParameter(search: String): HashMap<String, Any> {
        return hashMapOf(
            "query" to search,
            "sort" to "accuracy",
            "page" to 1,
            "size" to 80
        )
    }

    // 정규반 코드 참고
    private fun communicationNetWork(param: HashMap<String, Any>) = lifecycleScope.launch {
        val responseData = RetrofitClient.searchImageRetrofit.getImage(param)
        items = responseData.documents
        binding.searchRecyclerview.adapter = myAdapter
        hideKeyboard(requireActivity())
    }
}