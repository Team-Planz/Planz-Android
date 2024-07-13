import common.GradleUtil.implement

plugins {
    `java-library`
    id(app.Plugins.KOTLIN)
    id(app.Plugins.KOTLIN_KAPT)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    app.ModuleDependencies.javaInject.implement(this)
}
