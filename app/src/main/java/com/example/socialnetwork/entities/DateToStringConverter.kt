package com.example.socialnetwork.entities

import java.util.*
import java.util.concurrent.TimeUnit

object DateToStringConverter {
    fun getDaysAgo(date: Date): String? {
        val publish: Calendar = Calendar.getInstance()
        publish.time = date
        val msDiff: Long =
            Calendar.getInstance().timeInMillis - publish.timeInMillis
        val daysDiff: Long = TimeUnit.MILLISECONDS.toSeconds(msDiff)
//        return
//        if (daysDiff < 5) {
//            "a moment ago"
//        } else
        return  if (daysDiff < 60) {
            "$daysDiff " + "s"
//                    "second(-s) ago"
        } else if (daysDiff < 3600) {
            "" + TimeUnit.MILLISECONDS.toMinutes(msDiff).toString() + "m"
//                    " minute(-s) ago"
        } else if (daysDiff < 3600 * 24) {
            "" + TimeUnit.MILLISECONDS.toHours(msDiff).toString() + "h"
//                    " hours(-s) ago"
        } else {
            "" + TimeUnit.MILLISECONDS.toDays(msDiff).toString() + "d"
//                    " day(-s) ago"
        }
    }
}