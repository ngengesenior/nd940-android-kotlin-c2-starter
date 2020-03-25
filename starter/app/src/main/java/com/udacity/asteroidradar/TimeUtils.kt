package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*



class TimeUtils {

    companion object {

        fun getTodayDate() : String {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            return dateFormat.format(currentTime)
        }
    }

}