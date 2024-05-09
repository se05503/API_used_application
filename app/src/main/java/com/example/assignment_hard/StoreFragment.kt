package com.example.assignment_hard

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.assignment_hard.data.DocumentResponse
import com.example.assignment_hard.databinding.FragmentStoreBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreFragment : Fragment(),StoreItemDeleteListener {

    var storeImageList : ArrayList<DocumentResponse> ?= null
    var heartImageList : ArrayList<DocumentResponse> ?= null

    companion object {
        private var INSTANCE: StoreFragment? = null
        fun getFragment(): StoreFragment {
            return synchronized(StoreFragment::class.java) {
                val newInstance = INSTANCE ?: StoreFragment()
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    fun newInstance(param1: ArrayList<DocumentResponse>) =
        getFragment().apply {
            storeImageList = param1 // 아 데이터 갱신하는 과정이 너무 힘들게 느껴진다.. 이래서 livedata를 쓰는건가
        }

    private val binding by lazy { FragmentStoreBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        heartImageList = storeImageList?.filter { it.status == true } as ArrayList<DocumentResponse>
        Log.d("fragment data",storeImageList.toString())
        binding.storeRecyclerview.apply {
            adapter = heartImageList?.let { StoreAdapter(it, requireContext()) }
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun deleteItem(view: View, position: Int) {
        Log.d("isWork?","work")
        showDialog(position)
    }



    fun showDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("경고 메세지")
        builder.setMessage("정말 이미지를 보관함에서 지우시겠습니까?")
        builder.setIcon(R.drawable.ic_trashcan)

        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    heartImageList?.removeAt(position)
                    Log.d("deletedList",heartImageList.toString())
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    null
                }
            }
        }

        builder.setPositiveButton("삭제", listener)
        builder.setNegativeButton("취소", listener)
        builder.show()
    }


}