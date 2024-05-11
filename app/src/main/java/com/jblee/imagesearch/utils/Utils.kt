package com.jblee.imagesearch.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.jblee.imagesearch.model.SearchItemModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Utils 객체는 앱에서 공통적으로 사용되는 유틸리티 함수들을 포함하는 싱글턴 객체입니다.
 * 이를 통해 여러 위치에서 재사용 가능한 메서드들을 효과적으로 관리하고 사용할 수 있습니다.
*/
object Utils {

    /**
     * 주어진 타임스탬프 문자열의 형식을 변경하는 함수.
     *
     * @param timestamp 원래의 날짜/시간 문자열
     * @param fromFormatformat 원래의 날짜/시간 문자열 형식
     * @param toFormatformat 변경하려는 날짜/시간 문자열 형식
     * @return 변경된 형식의 날짜/시간 문자열
     */
    fun getDateFromTimestampWithFormat(
        timestamp: String?,
        fromFormatformat: String?,
        toFormatformat: String?
    ): String {
        var date: Date? = null
        var res = ""
        try {
            val format = SimpleDateFormat(fromFormatformat)
            date = format.parse(timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("jbdate", "getDateFromTimestampWithFormat date >> $date")

        val df = SimpleDateFormat(toFormatformat)
        res = df.format(date)
        return res
    }

    /**
     * Shared Preferences에 아이템을 추가하는 함수.
     *
     * @param context 앱의 현재 컨텍스트
     * @param item 추가하려는 아이템
     */
    fun addPrefItem(context: Context, item: SearchItemModel) {
        val prefs = context.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = GsonBuilder().create()
        editor.putString(item.url, gson.toJson(item))
        editor.apply()
    }

    /**
     * Shared Preferences에서 특정 URL을 키로 하는 아이템을 제거하는 함수.
     *
     * @param context 앱의 현재 컨텍스트
     * @param url 제거하려는 아이템의 URL
     */
    fun deletePrefItem(context: Context, url: String) {
        val prefs = context.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(url)
        editor.apply()
    }

    /**
     * Shared Preferences에서 모든 북마크 아이템을 가져오는 함수.
     *
     * @param context 앱의 현재 컨텍스트
     * @return 북마크된 아이템의 ArrayList
     */
    fun getPrefBookmarkItems(context: Context): ArrayList<SearchItemModel> {
        val prefs = context.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        val allEntries: Map<String, *> = prefs.all
        val bookmarkItems = ArrayList<SearchItemModel>()
        val gson = GsonBuilder().create()
        for ((key, value) in allEntries) {
            val item = gson.fromJson(value as String, SearchItemModel::class.java)
            bookmarkItems.add(item)
        }
        return bookmarkItems
    }
}