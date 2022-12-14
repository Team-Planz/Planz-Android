package com.yapp.growth.data.mapper

import com.yapp.growth.data.response.CreateTimeTableResponse
import com.yapp.growth.data.response.TimeRequestResponse
import com.yapp.growth.domain.entity.CreateTimeTable
import java.text.SimpleDateFormat
import java.util.*

private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
private val hourFormat = SimpleDateFormat("HH:mm", Locale.KOREA)

fun CreateTimeTableResponse.toCreateTimeTable(): CreateTimeTable {
    val response = this
    return CreateTimeTable(
        totalCount = response.totalCount,
        minTime = response.minTime,
        maxTime = response.maxTime,
        availableDates = response.availableDates,
        hourList = makeHourList(response.minTime, response.totalCount),
    )
}

fun TimeRequestResponse.toLong(): Long {
    return this.id
}
