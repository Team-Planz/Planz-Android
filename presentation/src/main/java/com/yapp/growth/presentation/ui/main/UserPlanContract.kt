package com.yapp.growth.presentation.ui.main

import com.yapp.growth.base.ViewEvent
import com.yapp.growth.base.ViewSideEffect
import com.yapp.growth.base.ViewState
import com.yapp.growth.domain.entity.UserPlanStatus

class UserPlanContract {
    data class UserPlanViewState(
        val userPlanStatus: UserPlanStatus = UserPlanStatus.UNKNOWN
    ): ViewState

    sealed class UserPlanSideEffect: ViewSideEffect {
        data class MoveToConfirmPlan(val planId: Long): UserPlanSideEffect()
        data class MoveToRespondPlan(val planId: Long): UserPlanSideEffect()
        data class MoveToMonitorPlan(val planId: Long): UserPlanSideEffect()
        object MoveToAlreadyConfirmPlan: UserPlanSideEffect()
    }

    sealed class UserPlanEvent: ViewEvent {
        data class GetUserPlanStatus(val planId: Long): UserPlanEvent()
    }
}
