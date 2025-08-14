/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.omega.build.Configuration
import java.time.LocalDateTime

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.hilt.plugin.get().pluginId)
}
apply(from = "$rootDir/android_common.gradle")
android {

    @Suppress("UnstableApiUsage")
    packagingOptions {
        jniLibs.keepDebugSymbols += "*/armeabi-v7a/libdu.so"
        jniLibs.keepDebugSymbols += "*/arm64-v8a/libdu.so"
    }

    compileSdk = Configuration.compileSdk
    namespace = Configuration.nameSpace
    @Suppress("UnstableApiUsage")
    defaultConfig {
        //applicationId = Configuration.debugNameSpace
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName
        vectorDrawables.useSupportLibrary = true
        ndk {
            abiFilters.clear()
            abiFilters += "armeabi-v7a"
            abiFilters += "arm64-v8a"
        }
        resourceConfigurations.addAll(listOf("en", "zh-rCN","zh-rTW","ms","vi","fil","ar"))
        println("USE_BUNDLE=${project.properties["USE_BUNDLE"]}")
        buildConfigField("boolean", "BuildBundle", project.properties["USE_BUNDLE"].toString())
        buildConfigField("String", "CV", "\"${Configuration.app.toUpperCase()}${Configuration.versionName}_Android\"")
        buildConfigField("String", "ConfigKey", "\"${Configuration.app}_android_sw_list\"")
        buildConfigField("String", "AppName", "\"${Configuration.app}\"")
    }
    bundle {
        language.enableSplit = false
    }

    flavorDimensions += "normal"

    @Suppress("UnstableApiUsage")
    signingConfigs {
        getByName("debug") {
            storeFile = file("${project.projectDir.absolutePath}/sign/debug.jks")
            storePassword = Configuration.debugSignPassWord
            keyAlias = Configuration.debugSignAlias
            keyPassword = Configuration.debugSignPassWord
        }
        create("release") {
            storeFile = file("${project.projectDir.absolutePath}/sign/release.jks")
            storePassword = Configuration.releaseSignPassWord
            keyAlias = Configuration.releaseSignAlias
            keyPassword = Configuration.releaseSignPassWord
        }
    }

    @Suppress("UnstableApiUsage")
    productFlavors {
        // ---- Todo 日常测试build ------
        create("normal") {
            applicationId = Configuration.debugApplicationId
            manifestPlaceholders["app_name"] = "${Configuration.app.capitalize()}-DEV"
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("String","Version","\"v${Configuration.versionName}.${LocalDateTime.now()}\"")
            resValue("string", "facebook_app_id", "xxxxxxxxxxxxxxx")
        }
        // ---- Todo 正式打包提审build ------
        create("package") {
            manifestPlaceholders["app_name"] = Configuration.app.capitalize()
            applicationId = Configuration.releaseApplicationId
            signingConfig = signingConfigs.getByName("release")
        }
    }

    @Suppress("UnstableApiUsage")
    buildTypes {
        getByName("debug") {
            signingConfig = null
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            setProperty("archivesBaseName", "${Configuration.app.toUpperCase()}_DEV_v${Configuration.versionCode}")
        }

        getByName("release") {
            signingConfig = null
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            setProperty("archivesBaseName", "${Configuration.app.toUpperCase()}_v${Configuration.versionCode}")
        }
    }

    androidComponents {
        beforeVariants {
            if(it.productFlavors.contains(Pair("normal", "package")) && it.buildType == "debug") {
                it.enable = false
            }
        }
    }


    hilt {
        enableAggregatingTask = true
    }

    kotlin {
        sourceSets.configureEach {
            kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
        }
    }

    @Suppress("UnstableApiUsage")
    lint {
        abortOnError = false
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
        // Determines whether to generate a BuildConfig class.
        // buildConfig = true
        // Determines whether to support Data Binding.
        // Note that the dataBinding.enabled property is now deprecated.
        // dataBinding = false
        // Determines whether to generate binder classes for your AIDL files.
        // aidl = true
        // Determines whether to support RenderScript.
        // renderScript = true
        // Determines whether to support injecting custom variables into the module’s R class.
        // resValues = true
        // Determines whether to support shader AOT compilation.
        // shaders = true
    }
}


dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-opengl"))
    // androidx
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.camera.base)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.startup)
    implementation(libs.unpeek)
    implementation("androidx.core:core-splashscreen:1.0.1")
    // di
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.compiler)
    // coroutines
    implementation(libs.coroutines)
    "normalImplementation"("com.squareup.leakcanary:leakcanary-android:2.14")
//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
//    //引入卡顿监控实现依赖库
//    debugImplementation("io.github.knight-zxw:blockcanary:0.0.5")
//    //引入卡顿消息通知及相关展示UI
//    debugImplementation("io.github.knight-zxw:blockcanary-ui:0.0.5")
}
apply(from = "$rootDir/exclude_other_version.gradle")