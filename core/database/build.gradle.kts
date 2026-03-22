plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serializable)
    alias(libs.plugins.jetbrains.kotlin.ksp)
}

android {
    namespace = "com.evg.database"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Room
    implementation(libs.room.common)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Dagger Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

    // Paging
    implementation(libs.jetpack.paging.runtime)
    implementation(libs.jetpack.paging.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}