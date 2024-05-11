package com.jblee.imagesearch.utils

import android.content.Context
import android.util.Log
import com.jblee.imagesearch.Constants.PREFS_NAME
import com.jblee.imagesearch.Constants.PREF_KEY
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 앱 전체에서 사용되는 유틸리티 함수들을 포함하는 객체입니다.
 */
object Utils {

    /**
     * 주어진 timestamp를 원하는 포맷으로 변환하여 반환합니다.
     *
     * @param timestamp 원본 timestamp 문자열
     * @param fromFormatformat 원본 timestamp의 포맷
     * @param toFormatformat 반환하고 싶은 날짜 포맷
     * @return 변환된 날짜 문자열
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
     * 마지막 검색어를 Shared Preferences에 저장합니다.
     *
     * @param context 호출하는 컨텍스트 (일반적으로 Activity 또는 Application)
     * @param query 저장하려는 검색어 문자열
     */
    fun saveLastSearch(context: Context, query: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_KEY, query).apply()
    }

    /**
     * Shared Preferences에서 마지막 검색어를 가져옵니다.
     *
     * @param context 호출하는 컨텍스트 (일반적으로 Activity 또는 Application)
     * @return 마지막으로 저장된 검색어 문자열. 저장된 값이 없으면 null을 반환합니다.
     */
    fun getLastSearch(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREF_KEY, null)
    }
}
