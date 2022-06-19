package com.yapp.growth.presentation.ui.splash

import com.yapp.growth.base.ViewEvent
import com.yapp.growth.base.ViewSideEffect
import com.yapp.growth.base.ViewState

class SplashContract {
    data class SplashViewState(
        val loginState: LoginState = LoginState.NONE
    ) : ViewState

    sealed class SplashSideEffect : ViewSideEffect {
    }

    sealed class SplashEvent : ViewEvent {
    }

    enum class LoginState {
        NONE, SUCCESS, REQUIRED
    }
}