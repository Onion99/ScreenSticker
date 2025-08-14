import com.omega.build.Configuration
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}
apply(from = "$rootDir/android_common.gradle")
android {
    namespace = "com.omega.common"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        minSdk = Configuration.minSdk
    }
}
dependencies {
    implementation(libs.coroutines)
    api(libs.timber)
}
apply(from = "$rootDir/exclude_other_version.gradle")
