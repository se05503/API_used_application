package com.example.assignment_hard

import com.example.assignment_hard.data.DocumentResponse

interface StoreItemDeleteListener { // interface 이름을 listener라고 칭하는건가?
    fun deleteItem(position:Int)
//    fun heartDetachedDeleteItem(item: DocumentResponse)
}