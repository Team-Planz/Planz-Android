package com.yapp.growth.data.mapper

import com.yapp.growth.data.response.PromisingTimeTableResponse
import com.yapp.growth.data.response.TimeTableDateResponse
import com.yapp.growth.data.response.TimeTableUnitResponse
import com.yapp.growth.domain.entity.TimeTable
import com.yapp.growth.domain.entity.TimeTableDate
import com.yapp.growth.domain.entity.TimeTableUnit
import java.text.SimpleDateFormat
import java.util.*

private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
private val hourFormat = SimpleDateFormat("HH:mm", Locale.KOREA)

fun PromisingTimeTableResponse.toTimeTable(): TimeTable {
    val response = this
    return TimeTable(
        users = response.members.toUserList(),
        colors = response.colors,
        totalCount = response.totalCount,
        timeTableDate = response.timeTable.toTimeTableDateList(),
        id = response.id,
        promisingName = response.promisingName,
        owner = response.owner.toUser(),
        minTime = response.minTime,
        maxTime = response.maxTime,
        availableDates = response.availableDates,
        hourList = makeHourList(response.minTime, response.totalCount),
        placeName = response.placeName,
        categoryName = response.category.keyword,
        category = response.category.toCategory(),
    )
}


fun List<TimeTableDateResponse>.toTimeTableDateList(): List<TimeTableDate> = map { it.toTimeTableDate() }
fun TimeTableDateResponse.toTimeTableDate(): TimeTableDate {
    val response = this
    return TimeTableDate(
        date = response.date,
        timeTableUnits = response.blocks.toTimeTableUnitList()
    )
}

fun List<TimeTableUnitResponse>.toTimeTableUnitList(): List<TimeTableUnit> = map { it.toTimeTableUnit() }
fun TimeTableUnitResponse.toTimeTableUnit(): TimeTableUnit {
    val response = this
    return TimeTableUnit(
        index = response.index,
        count = response.count,
        users = response.users.toUserList(),
        color = response.color
    )
}

fun makeHourList(minTime: String, totalCount: Int): List<String> {
    val calendar = Calendar.getInstance().apply {
        time = parseFormat.parse(minTime) ?: Date()
    }

    val hourList = mutableListOf<String>().also { hourList ->
        repeat(totalCount) {
            hourList.add(hourFormat.format(calendar.time))
            calendar.add(Calendar.HOUR, 1)
        }
    }.toList()

    return hourList
}
