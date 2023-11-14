import common.GradleUtil.implement

plugins {
    id(app.Plugins.ANDROID_LIBRARY)
    id(app.Plugins.KOTLIN_ANDROID)
    id(app.Plugins.KOTLIN_KAPT)
    id(app.Plugins.HILT_ANDROID)
    id(app.Plugins.SECRETS_GRADLE)
}

android {
    namespace = "com.yapp.growth.data"
    compileSdk = Configs.COMPILE_SDK
    defaultConfig {
        minSdk = Configs.MIN_SDK
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))

    app.ModuleDependencies.androidCore.implement(this)
    app.ModuleDependencies.coroutines.implement(this)
    app.ModuleDependencies.retrofit.implement(this)
    app.ModuleDependencies.okhttp.implement(this)
    app.ModuleDependencies.hilt.implement(this)
    app.ModuleDependencies.timber.implement(this)
    app.ModuleDependencies.kakaoOAuth.implement(this)
    app.ModuleDependencies.kotlinDateTime.implement(this)

    implementation(platform(app.ModuleDependencies.FIREBASE_BOM))
    implementation(app.ModuleDependencies.FIREBASE_ANALYTICS)
    implementation(app.ModuleDependencies.FIREBASE_CRASHLYTICS)
}
