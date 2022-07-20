package com.yapp.growth.data.source

import com.yapp.growth.data.api.GrowthApi
import com.yapp.growth.data.api.handleApi
import com.yapp.growth.domain.NetworkResult
import com.yapp.growth.domain.entity.Plan.FixedPlan
import toFixedPlan
import javax.inject.Inject

internal class AllFixedPlanDataSourceImpl @Inject constructor(
    private val retrofitApi: GrowthApi
) : AllFixedPlanDataSource {

    override suspend fun getAllFixedPlanList(): NetworkResult<List<FixedPlan>> =
        handleApi {
            retrofitApi.getAllFixedPlanList().map {
                it.toFixedPlan()
            }
        }
}
