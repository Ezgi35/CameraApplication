

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.cameraapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.cameraapplication"
        minSdk = 26
        targetSdk = 33
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

}

dependencies {

    implementation ("androidx.camera:camera-lifecycle:1.0.0")
    implementation ("androidx.camera:camera-view:1.0.0-alpha26")
    implementation ("log4j:log4j:1.2.17")
    implementation ("com.jcraft:jsch:0.1.55")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")







    implementation ("com.soundcloud.android:android-crop:1.0.1")
    implementation ("com.soundcloud.android:android-crop:1.0.1")

    //implementation fileTree("dir: 'libs', include: ['*.jar']")

    implementation ("androidx.appcompat:appcompat:1.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:1.1.3")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("androidx.test.ext:junit:1.1.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.2.0")

    implementation ("androidx.cardview:cardview:1.0.0")



    implementation ("androidx.camera:camera-camera2:1.0.0-beta05")
    implementation ("androidx.camera:camera-lifecycle:1.0.0-beta05")
    implementation ("androidx.camera:camera-view:1.0.0-alpha12")
    implementation ("androidx.camera:camera-extensions:1.0.0-alpha12")

}