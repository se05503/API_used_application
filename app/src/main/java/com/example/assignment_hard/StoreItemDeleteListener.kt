package com.example.assignment_hard

import android.view.View
import com.example.assignment_hard.data.DocumentResponse

interface StoreItemDeleteListener { // interface 이름을 listener라고 칭하는건가?
    fun deleteItem(view: View, position:Int)
//    fun heartDetachedDeleteItem(item: DocumentResponse)
}