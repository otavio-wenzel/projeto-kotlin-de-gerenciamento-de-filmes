plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

android {
    namespace = "com.example.cine"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cine"
        minSdk = 24
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
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependências do Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    //annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    //
    implementation("androidx.core:core-ktx:1.9.0")

    // Jetpack Compose BOM (versão segura com Compose 1.5.4)
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))

    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    //

    implementation("androidx.compose.ui:ui-graphics")

    // Compose Tooling (Preview e Debug)
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Activity + Compose
    implementation("androidx.activity:activity-compose:1.8.0")

    // ViewModel com Compose (MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Runtime Compose
    implementation("androidx.compose.runtime:runtime")

    // Kotlin Standard Library (opcional com Kotlin Android plugin)
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Testes (opcional)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Ícones (precisa para Icons.Default.*)
    implementation ("androidx.compose.material:material-icons-extended")

    // FlowRow (fundação de layouts; evita depender de Accompanist)
    implementation ("androidx.compose.foundation:foundation-layout")
}