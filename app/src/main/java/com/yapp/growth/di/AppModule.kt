package com.yapp.growth.di

import com.yapp.growth.LoginSdk
import com.yapp.growth.app.BuildConfig
import com.yapp.growth.data.api.NetworkSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkSettings(): NetworkSettings {
        return NetworkSettings(
            connectTimeoutMs = 10_000,
            readTimeoutMs = 10_000,
            isDebugMode = BuildConfig.DEBUG
        )
    }

    @Provides
    @Singleton
    fun provideInterceptors(
        kakaoLoginSdk: LoginSdk,
    ): Interceptor {
        return Interceptors(kakaoLoginSdk)
    }

    @Provides
    @Singleton
    fun provideAuthenticator(
        kakaoLoginSdk: LoginSdk,
    ): Authenticator {
        return KakaoAccessTokenAuthenticator(kakaoLoginSdk)
    }
}
