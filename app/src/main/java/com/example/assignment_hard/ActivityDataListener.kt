package com.example.assignment_hard

import com.example.assignment_hard.data.DocumentResponse

interface ActivityDataListener {
    fun onDataReceived(data:DocumentResponse)
}