package com.yapp.growth.presentation.ui.main.home

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.yapp.growth.base.LoadState
import com.yapp.growth.base.ViewEvent
import com.yapp.growth.base.ViewSideEffect
import com.yapp.growth.base.ViewState
import com.yapp.growth.domain.entity.Plan

class HomeContract {
    data class HomeViewState(
        val loadState: LoadState = LoadState.LOADING,
        val todayPlanLoadState: LoadState = LoadState.LOADING,
        val monthlyPlanLoadState: LoadState = LoadState.LOADING,
        val loginState: LoginState = LoginState.LOGIN,
        val userName: String = "",
        val calendarPlans: List<Plan.FixedPlan> = emptyList(),
        val todayPlans: List<Plan.FixedPlan> = emptyList(),
        val monthlyPlans: List<Plan.FixedPlan> = emptyList(),
        val selectDayPlans: List<Plan.FixedPlan> = emptyList(),
        val selectionDay: CalendarDay = CalendarDay.today(),
        val isTodayPlanExpanded: Boolean = false,
        val isMonthlyPlanExpanded: Boolean = false,
        val monthlyPlanMode: MonthlyPlanModeState = MonthlyPlanModeState.CALENDAR
    ) : ViewState

    sealed class HomeSideEffect : ViewSideEffect {
        data class ShowSnackBar(val msg: String) : HomeSideEffect()
        object MoveToLogin : HomeSideEffect()
        object NavigateToMyPageScreen : HomeSideEffect()
        data class NavigateDetailPlanScreen(val planId: Int) : HomeSideEffect()
        object ShowBottomSheet : HomeSideEffect()
    }

    sealed class HomeEvent : ViewEvent {
        object InitHomeScreen : HomeEvent()
        object OnInduceLoginClicked : HomeEvent()
        object OnUserImageClicked : HomeEvent()
        data class OnPlanItemClicked(val planId: Int) : HomeEvent()
        data class OnCalendarDayClicked(val selectionDay: CalendarDay) : HomeEvent()
        object OnTodayPlanExpandedClicked : HomeEvent()
        object OnMonthlyPlanExpandedClicked : HomeEvent()
        object OnMonthlyPlanModeClicked : HomeEvent()
        object OnMonthlyPreviousClicked : HomeEvent()
        object OnMonthlyNextClicked : HomeEvent()
    }

    enum class LoginState {
        NONE, LOGIN
    }

    enum class MonthlyPlanModeState {
        CALENDAR, TEXT
    }
}
