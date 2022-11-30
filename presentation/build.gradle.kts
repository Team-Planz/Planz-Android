import common.GradleUtil.implement

plugins {
    id(app.Plugins.ANDROID_LIBRARY)
    id(app.Plugins.KOTLIN_ANDROID)
    id(app.Plugins.KOTLIN_PARCELIZE)
    id(app.Plugins.KOTLIN_KAPT)
    id(app.Plugins.GOOGLE_SERVICE)
    id(app.Plugins.FIREBASE_CRASHLYTICS)
    id(app.Plugins.HILT_ANDROID)
    id(app.Plugins.SECRETS_GRADLE)
}

android {
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))
    app.ModuleDependencies.androidCore.implement(this)
    app.ModuleDependencies.compose.implement(this)
    app.ModuleDependencies.hilt.implement(this)
    app.ModuleDependencies.hiltAndroid.implement(this)
    app.ModuleDependencies.timber.implement(this)
    app.ModuleDependencies.materialCalendarView.implement(this)
    app.ModuleDependencies.accompanist.implement(this)
    app.ModuleDependencies.kotlinDateTime.implement(this)
    app.ModuleDependencies.kakaoOAuth.implement(this)
    app.ModuleDependencies.kakaoShare.implement(this)
    app.ModuleDependencies.shimmer.implement(this)

    implementation(platform(app.ModuleDependencies.FIREBASE_BOM))
    implementation(app.ModuleDependencies.FIREBASE_ANALYTICS)
    implementation(app.ModuleDependencies.FIREBASE_CRASHLYTICS)
    implementation(app.ModuleDependencies.FIREBASE_DYNAMICLINKS)
}
