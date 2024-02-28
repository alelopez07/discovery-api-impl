plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.jorgealejandro.tm.discoveryapi.challenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.jorgealejandro.tm.discoveryapi.challenge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    name = "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
        debug {
            isDefault = true
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
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
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    //region modules
    implementation(project(":core"))
    implementation(project(":data"))
    //endregion modules

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.fragment.ktx)
    implementation(libs.activity)
    implementation(libs.constraintLayout)

    implementation(libs.paging.common)
    implementation(libs.paging.ktx)

    implementation(libs.lifecycle.liveData)
    implementation(libs.lifecycle.viewModel)
    implementation(libs.lifecycle.extensions)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

kapt {
    correctErrorTypes = true
}