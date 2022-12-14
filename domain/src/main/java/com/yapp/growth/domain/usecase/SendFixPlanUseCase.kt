package com.yapp.growth.domain.usecase

import com.yapp.growth.domain.NetworkResult
import com.yapp.growth.domain.entity.Plan
import com.yapp.growth.domain.repository.FixPlanRepository
import javax.inject.Inject

class SendFixPlanUseCase @Inject constructor(
    private val repository: FixPlanRepository
) {
    suspend operator fun invoke(planId: Long, date: String): NetworkResult<Plan.FixedPlan> {
        return repository.sendFixPlan(planId, date)
    }
}
