package com.yapp.growth.data.internal.source

import com.yapp.growth.data.api.GrowthApi
import com.yapp.growth.data.api.handleApi
import com.yapp.growth.data.mapper.*
import com.yapp.growth.data.parameter.FixPlanParameter
import com.yapp.growth.data.parameter.TemporaryPlanParameter
import com.yapp.growth.data.parameter.TimeCheckedOfDayParameter
import com.yapp.growth.data.parameter.TimeCheckedOfDaysParameter
import com.yapp.growth.data.source.PlanzDataSource
import com.yapp.growth.domain.NetworkResult
import com.yapp.growth.domain.entity.*
import javax.inject.Inject

internal class PlanzDataSourceImpl @Inject constructor(
    private val retrofitApi : GrowthApi
): PlanzDataSource {

    override suspend fun getCreateTimeTable(uuid: String): NetworkResult<CreateTimeTable> =
        handleApi {
            retrofitApi.getCreateTimeTable(uuid).toCreateTimeTable()
        }

    override suspend fun makePlan(
        uuid: String,
        timeCheckedOfDays: List<TimeCheckedOfDay>
    ): NetworkResult<Long> =
        handleApi {
            val parameter = TimeCheckedOfDaysParameter(
                unit = 0.5f,
                timeTable = timeCheckedOfDays.map {
                    TimeCheckedOfDayParameter(
                        date = it.date,
                        times = it.timeList
                    )
                }
            )
            retrofitApi.makePlan(uuid, parameter).toLong()
        }

    override suspend fun getRespondUsers(promisingId: Long): NetworkResult<TimeTable> =
        handleApi {
            retrofitApi.getResponseTimeTable(promisingId.toString()).toTimeTable()
        }

    override suspend fun sendRespondPlan(
        promisingId: Long,
        timeCheckedOfDays: List<TimeCheckedOfDay>
    ): NetworkResult<Unit> =
        handleApi {
            val parameter = TimeCheckedOfDaysParameter(
                unit = 0.5f,
                timeTable = timeCheckedOfDays.map {
                    TimeCheckedOfDayParameter(
                        date = it.date,
                        times = it.timeList
                    )
                }
            )
            retrofitApi.sendRespondPlan(promisingId.toString(), parameter)
        }

    override suspend fun sendFixPlan(promisingId: Long, date: String): NetworkResult<Any> =
        handleApi {
            retrofitApi.sendFixPlan(promisingId.toString(), FixPlanParameter(date))
        }

    override suspend fun getFixedPlans(): NetworkResult<List<Plan.FixedPlan>> =
        handleApi {
            retrofitApi.getFixedPlans().map { it.toFixedPlan() }
        }

    override suspend fun getFixedPlan(planId: Long): NetworkResult<Plan.FixedPlan> =
        handleApi {
            retrofitApi.getFixedPlan(planId).toFixedPlan()
        }

    override suspend fun getWaitingPlans(): NetworkResult<List<Plan.WaitingPlan>> =
        handleApi {
            retrofitApi.getWaitingPlans().map { it.toWaitingPlan() }
        }

    override suspend fun createTemporaryPlan(temporaryPlanParameter: TemporaryPlanParameter): NetworkResult<TemporaryPlanUuid> =
        handleApi {
            retrofitApi.createTemporaryPlan(temporaryPlanParameter).toTemporaryPlanUuid()
        }

    override suspend fun signUp(): NetworkResult<User> =
        handleApi {
            retrofitApi.signUp().toUser()
        }

    override suspend fun getUserInfo(): NetworkResult<User> =
        handleApi {
            retrofitApi.getUserInfo().toUser()
        }
}
