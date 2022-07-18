package com.yapp.growth.presentation.ui.main.manage.confirm

import com.yapp.growth.base.ViewEvent
import com.yapp.growth.base.ViewSideEffect
import com.yapp.growth.base.ViewState
import com.yapp.growth.domain.entity.ResponsePlan
import com.yapp.growth.domain.entity.User

class ConfirmPlanContract {
    data class ConfirmPlanViewState(
        val responsePlan: ResponsePlan = ResponsePlan(emptyList(), emptyList(), emptyList(),0,"","", emptyList(), emptyList()),
        val currentClickTimeIndex: Pair<Int, Int> = -1 to -1,
        val currentClickUserData: List<User> = emptyList()
    ) : ViewState

    sealed class ConfirmPlanSideEffect : ViewSideEffect {
        object ShowBottomSheet : ConfirmPlanSideEffect()
        object HideBottomSheet : ConfirmPlanSideEffect()
    }

    sealed class ConfirmPlanEvent : ViewEvent {
        object OnClickNextDayButton : ConfirmPlanEvent()
        object OnClickPreviousDayButton : ConfirmPlanEvent()
        data class OnClickTimeTable(val dateIndex: Int, val minuteIndex: Int) : ConfirmPlanEvent()
        data class OnClickConfirmButton(val dateIndex: Int, val minuteIndex: Int) :
            ConfirmPlanEvent()
    }
}