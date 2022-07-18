package com.yapp.growth.domain.repository

import com.yapp.growth.domain.NetworkResult
import com.yapp.growth.domain.entity.ResponsePlan

interface ConfirmPlanRepository {

    suspend fun getRespondUsers(promisingKey: Long): NetworkResult<ResponsePlan>
}