package com.yapp.growth.domain.repository

import com.yapp.growth.domain.NetworkResult
import com.yapp.growth.domain.entity.User
import com.yapp.growth.domain.entity.UserPlanStatus

interface UserRepository {
    suspend fun signUp(): NetworkResult<User>

    suspend fun modifyNickName(nickName: String): NetworkResult<User>
    suspend fun getUserInfo(): NetworkResult<User>

    fun getCachedUserInfo(): User?
    fun removeCachedUserInfo()

    suspend fun getUserPlanStatus(planId: Long): NetworkResult<UserPlanStatus>
    suspend fun removeUserInfo(): NetworkResult<Unit>
}
