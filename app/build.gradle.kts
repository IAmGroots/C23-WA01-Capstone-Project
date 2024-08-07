plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.capstoneproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.capstoneproject"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL_MIDTRANS", "\"...\"")
        buildConfigField("String", "AUTH_KEY_MIDTRANS", "\"...\"")
        buildConfigField("String", "MERCHANT_SERVER", "\"...\"")
        buildConfigField("String", "CLIENT_KEY", "\"...\"")
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
        buildConfig = true
    }
}

dependencies {

    // default
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("androidx.room:room-common:2.6.0")

    // kalau bisa beritahu dependecies nya untuk apa pakai komentar
    // ex. glide, retrofit, dll

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // circle image view
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //firebase
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.firebaseui:firebase-ui-database:8.0.0")

    // fingerprint biometric
    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // lottie animation
    implementation(group = "com.airbnb.android", name = "lottie", version = "6.0.0")

    // google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // midtrans
    implementation("com.midtrans:uikit:2.0.0-SANDBOX")

    // networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // swipe refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // tab layout
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // otp
    implementation("com.github.1902shubh:SendMail:1.0.0")

    // chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // slider
    implementation("com.github.smarteist:autoimageslider:1.4.0")

    // skeleton
    implementation("com.ericktijerou.koleton:koleton:1.0.0-beta01")

    // testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}