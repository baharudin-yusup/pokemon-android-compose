import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.baharudin.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    flavorDimensions += "environment"
    
    productFlavors {
        create("dev") {
            dimension = "environment"
            
            buildConfigField("String", "API_BASE_URL", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("long", "API_CONNECT_TIMEOUT", "60L")
            buildConfigField("long", "API_READ_TIMEOUT", "60L")
            buildConfigField("long", "API_WRITE_TIMEOUT", "60L")
            buildConfigField("boolean", "ENABLE_API_LOGGING", "true")
        }
        
        create("production") {
            dimension = "environment"
            
            buildConfigField("String", "API_BASE_URL", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("long", "API_CONNECT_TIMEOUT", "30L")
            buildConfigField("long", "API_READ_TIMEOUT", "30L")
            buildConfigField("long", "API_WRITE_TIMEOUT", "30L")
            buildConfigField("boolean", "ENABLE_API_LOGGING", "false")
        }
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "DEBUG_MODE", "true")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "DEBUG_MODE", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    buildFeatures {
        buildConfig = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    
    // Serialization
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.common)
    implementation(libs.room.paging)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Logging
    implementation(libs.timber)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}