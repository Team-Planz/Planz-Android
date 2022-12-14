package com.yapp.growth.presentation.ui.main.manage

import com.yapp.growth.base.LoadState
import com.yapp.growth.base.ViewEvent
import com.yapp.growth.base.ViewSideEffect
import com.yapp.growth.base.ViewState
import com.yapp.growth.domain.entity.Plan

class ManageContract {
    data class ManageViewState(
        val waitingPlansLoadState: LoadState = LoadState.SUCCESS,
        val fixedPlansLoadState: LoadState = LoadState.SUCCESS,
        val waitingPlans: List<Plan.WaitingPlan> = emptyList(),
        val fixedPlans: List<Plan.FixedPlan> = emptyList(),
    ) : ViewState

    sealed class ManageSideEffect : ViewSideEffect {
        object NavigateToCreateScreen : ManageSideEffect()
        data class NavigateToFixPlanScreen(val planId: Int) : ManageSideEffect()
        data class NavigateToMemberResponseScreen(val planId: Int) : ManageSideEffect()
        data class NavigateToMonitorPlanScreen(val planId: Int) : ManageSideEffect()
        data class NavigateToDetailPlanScreen(val planId: Int) : ManageSideEffect()
        data class SwitchTab(val tabIndex: Int) : ManageSideEffect()
    }

    sealed class ManageEvent : ViewEvent {
        object InitManageScreen : ManageEvent()
        object OnClickCreateButton : ManageEvent()
        data class OnClickWaitingPlan(val planId: Int) : ManageEvent()
        data class OnClickFixedPlan(val planId: Int) : ManageEvent()
        data class OnClickTab(val tabIndex: Int) : ManageEvent()
    }
}
