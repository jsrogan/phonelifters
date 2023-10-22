plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "edu.umich.phonelifters"
    compileSdk = 33

    defaultConfig {
        applicationId = "edu.umich.phonelifters"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.06.01")) // 2023.06.01 max for API 33
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.compose.material:material:1.5.0-beta03") // beta03 max for API 33
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.6.0") // 2.6.0 max for API 33
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
    implementation("com.google.ar:core:1.39.0")
    implementation("com.google.ar.sceneform.ux:sceneform-ux:1.9.0")
    implementation("io.github.sceneview:arsceneview:1.1.0")
    //implementation files("/home/alex/Desktop/phonelifters/source_code/PocketTrainer/app/src/main/java/edu/umich/phonelifters/arcore-android-sdk")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}