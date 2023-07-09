import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.serialization)
    alias(libs.plugins.parcelize)
}

val spotifyClientID = gradleLocalProperties(rootDir).getProperty("spotify_client_id")!!
val spotifyClientSecret = gradleLocalProperties(rootDir).getProperty("spotify_client_secret")!!
val releaseStoreFile = project.properties["RELEASE_STORE_FILE"] as String?
val releaseStorePassword = project.properties["RELEASE_STORE_PASSWORD"] as String?
val releaseKeyAlias = project.properties["RELEASE_KEY_ALIAS"] as String?
val releaseKeyPassword = project.properties["RELEASE_KEY_PASSWORD"] as String?

android {
    namespace = "pl.lambada.songsync"
    compileSdk = 33

    defaultConfig {
        applicationId = "pl.lambada.songsync"
        minSdk = 30
        targetSdk = 33
        versionCode = 13
        versionName = "1.3"

        resourceConfigurations += arrayOf(
            "en", "es", "de"
        )

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"$spotifyClientID\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"$spotifyClientSecret\"")
    }
    signingConfigs {
        create("release") {
            if (project.hasProperty("RELEASE_KEY_ALIAS")) {
                storeFile = file(releaseStoreFile!!)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (project.hasProperty("RELEASE_KEY_ALIAS")) {
                signingConfig = signingConfigs["release"]
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        android.buildFeatures.buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.paging.common.ktx)
    debugImplementation(libs.ui.tooling)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.json)
    implementation(libs.commons.text)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.coil)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.android)
}