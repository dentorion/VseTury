plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Coroutines
    Dependencies.coroutines.apply {
        api(androidCoroutines)
    }

    // @Inject
    implementation("javax.inject:javax.inject:1")
}