plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "uk.co.timesheets24.app.TS24"
    compileSdk = 35

    defaultConfig {
        applicationId = "uk.co.timesheets24.app.TS24"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"

            )
            buildConfigField("String", "AUTH_BASE_URL", "\"https://portal.timesheets24.co.uk/api/auth/\"")
            buildConfigField("String", "ACCOUNT_URL", "\"https://portal.timesheets24.co.uk/api/account/\"")
            buildConfigField("String", "PROFILE_URL", "\"https://portal.timesheets24.co.uk/api/profile/\"")
            buildConfigField("String", "BASE_URL", "\"https://portal.timesheets24.co.uk/api/auth/\"")
            buildConfigField("String", "CLIENT_URL", "\"https://portal.timesheets24.co.uk/api/clients/\"")
            buildConfigField("String", "ACCOUNTMI_URL", "\"https://portal.timesheets24.co.uk/api/accountmi/\"")
            buildConfigField("String", "ADMIN_URL", "\"https://portal.timesheets24.co.uk/api/admin/\"")
            buildConfigField("String", "FILES_URL", "\"https://portal.timesheets24.co.uk/api/files/\"")
            buildConfigField("String", "JOBS_URL", "\"https://portal.timesheets24.co.uk/api/jobs/\"")
        }
        debug {

//            buildConfigField("String", "AUTH_BASE_URL", "\"https://portal.timesheets24.co.uk/api/auth/\"")
//            buildConfigField("String", "ACCOUNT_URL", "\"https://portal.timesheets24.co.uk/api/account/\"")
//            buildConfigField("String", "PROFILE_URL", "\"https://portal.timesheets24.co.uk/api/profile/\"")
//            buildConfigField("String", "BASE_URL", "\"https://portal.timesheets24.co.uk/api/auth/\"")
//            buildConfigField("String", "CLIENT_URL", "\"https://portal.timesheets24.co.uk/api/clients/\"")
//            buildConfigField("String", "ACCOUNTMI_URL", "\"https://portal.timesheets24.co.uk/api/accountmi/\"")
//            buildConfigField("String", "ADMIN_URL", "\"https://portal.timesheets24.co.uk/api/admin/\"")
//            buildConfigField("String", "FILES_URL", "\"https://portal.timesheets24.co.uk/api/files/\"")
//            buildConfigField("String", "JOBS_URL", "\"https://portal.timesheets24.co.uk/api/jobs/\"")

            buildConfigField("String", "BASE_URL", "\"https://10.0.2.2/\"")
            buildConfigField("String", "AUTH_BASE_URL", "\"https://10.0.2.2:44306/\"")
            buildConfigField("String", "PROFILE_URL", "\"https://10.0.2.2:44310/\"")
            buildConfigField("String", "ACCOUNT_URL", "\"https://10.0.2.2:44303/\"")
            buildConfigField("String", "CLIENT_URL", "\"https://10.0.2.2:44304/\"")
            buildConfigField("String", "ACCOUNTMI_URL", "\"https://10.0.2.2:44305/\"")
            buildConfigField("String", "ADMIN_URL", "\"https://10.0.2.2:44307/\"")
            buildConfigField("String", "FILES_URL", "\"https://10.0.2.2:44308/\"")
            buildConfigField("String", "JOBS_URL", "\"https://10.0.2.2:44309/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // For JSON parsing
    implementation(libs.okhttp)
    implementation(libs.androidx.work.runtime.ktx.v281)
    implementation(libs.retrofit)
    implementation ("com.github.tehras:charts:0.2.4-alpha")
    implementation (libs.coil.compose)
    androidTestImplementation (libs.androidx.espresso.core.v351)
    testImplementation(kotlin("test"))
    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler.v272)
    implementation(libs.maps.compose)
    implementation("androidx.compose.material:material:1.6.1")
    implementation("com.google.accompanist:accompanist-insets:0.30.1")

}