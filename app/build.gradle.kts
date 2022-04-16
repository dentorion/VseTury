plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        applicationId = "com.entin.vsetury"
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = 1
        versionName = "1"
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
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))

    implementation(project(mapOf("path" to ":firebase")))
    implementation(project(mapOf("path" to ":room")))

    // Main dependencies of project
    Dependencies.base.apply {
        implementation(coreKtx)
        implementation(appcompat)
        implementation(material)
        implementation(constraintlayout)
        implementation(fragment)
    }

    // Hilt
    Dependencies.hilt.apply {
        implementation(mainHilt)
        kapt(compileAndroid)
    }

    // ROOM
    Dependencies.room.apply {
        implementation(runtime)
        kapt(compiler)
        implementation(ktx)
    }

    // NAVIGATION COMPONENT
    Dependencies.navigation.apply {
        implementation(mainNavigation)
        implementation(ui)
    }

    // Lifecycle + ViewModel & LiveData
    Dependencies.lifecycle.apply {
        implementation(lifecycle)
        implementation(liveData)
        implementation(viewModel)
    }

    // Gson
    Dependencies.gson.apply {
        implementation(gson)
    }

    // DATA STORE
    Dependencies.dataStore.apply {
        implementation(dataStore)
    }

    // Recyclerview
    Dependencies.recyclerview.apply {
        implementation(recyclerview)
    }

    // Firebase
    Dependencies.firebase.apply {
        implementation(platform(bom))
        implementation(crashlytics)
        implementation(analytics)
//        implementation(auth)
//        implementation(playServicesAuth)
    }

    // Timber
    Dependencies.timber.apply {
        implementation(timber)
    }

    // ViewPager 2
    Dependencies.viewPager.apply {
        implementation(viewPager)
    }

    // No INTERNET Connection
    Dependencies.connection.apply {
        implementation(oops)
    }

    // Glide
    Dependencies.glide.apply {
        implementation(glide)
    }

    // Leak Canary
    Dependencies.leakCanary.apply {
        debugImplementation(canary)
    }

    // Splash screen
    Dependencies.splashScreen.apply {
        implementation(splashScreen)
    }
}

kapt {
    correctErrorTypes = true
}