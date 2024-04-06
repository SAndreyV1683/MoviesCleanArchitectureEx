plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-android")
    id ("kotlin-kapt")
}

android {

    namespace = "com.example.moviescleanarchitectureex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moviescleanarchitectureex"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }


}

val archComponents = "2.7.0"

dependencies {


    implementation ("com.google.dagger:dagger:2.50")
    implementation("com.google.android.ads:mediation-test-suite:3.0.0")
    kapt ("com.google.dagger:dagger-compiler:2.50")
    kapt ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$archComponents")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$archComponents")
    implementation("androidx.lifecycle:lifecycle-common-java8:$archComponents")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$archComponents")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.hannesdorfmann:adapterdelegates4-kotlin-dsl:4.3.2")
    implementation ("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:4.3.2")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")



}