import com.android.build.api.dsl.Packaging
import com.omega.build.Configuration
import java.time.LocalDateTime
import java.util.Locale

plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.hilt.plugin.get().pluginId)
    alias(libs.plugins.compose.compiler)
}
apply(from = "$rootDir/android_common.gradle")
android {

    fun Packaging.() {
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
            //noinspection ChromeOsAbiSupport
            abiFilters += "armeabi-v7a"
            //noinspection ChromeOsAbiSupport
            abiFilters += "arm64-v8a"
        }
        androidResources.localeFilters.addAll(listOf("en", "zh-rCN","zh-rTW","ms","vi","fil","ar"))
        println("USE_BUNDLE=${project.properties["USE_BUNDLE"]}")
        buildConfigField("boolean", "BuildBundle", project.properties["USE_BUNDLE"].toString())
        buildConfigField("String", "CV", "\"${Configuration.app}${Configuration.versionName}_Android\"")
        buildConfigField("String", "ConfigKey", "\"${Configuration.app}_android_sw_list\"")
        buildConfigField("String", "AppName", "\"${Configuration.app}\"")
    }
    bundle {
        language.enableSplit = false
    }

    flavorDimensions += "normal"

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

    productFlavors {
        // ---- 日常测试build ------
        create("normal") {
            applicationId = Configuration.debugApplicationId
            manifestPlaceholders["app_name"] = "${Configuration.app}-DEV"
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("String","Version","\"v${Configuration.versionName}.${LocalDateTime.now()}\"")
            resValue("string", "facebook_app_id", "xxxxxxxxxxxxxxx")
        }
        // ---- 正式打包提审build ------
        create("package") {
            manifestPlaceholders["app_name"] = Configuration.app
            applicationId = Configuration.releaseApplicationId
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = null
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            setProperty("archivesBaseName", "${Configuration.app}_DEV_v${Configuration.versionCode}")
        }

        getByName("release") {
            signingConfig = null
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            setProperty("archivesBaseName", "${Configuration.app}_v${Configuration.versionCode}")
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

    lint {
        abortOnError = false
    }

    buildFeatures {
        compose = true
        //viewBinding = true
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
    implementation(projects.coreUi)
    // androidx
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.startup)
    implementation(libs.unpeek)
    //noinspection UseTomlInstead
    implementation("androidx.core:core-splashscreen:1.0.1")
    // di
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.compiler)
    // coroutines
    implementation(libs.coroutines)
    //noinspection UseTomlInstead
    "normalImplementation"("com.squareup.leakcanary:leakcanary-android:2.14")
    //debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
    //引入卡顿监控实现依赖库
    //debugImplementation("io.github.knight-zxw:blockcanary:0.0.5")
    //引入卡顿消息通知及相关展示UI
    //debugImplementation("io.github.knight-zxw:blockcanary-ui:0.0.5")
}
apply(from = "$rootDir/exclude_other_version.gradle")