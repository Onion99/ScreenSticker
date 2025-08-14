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


@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
}
apply(from = "$rootDir/android_common.gradle")
android {
  compileSdk = Configuration.compileSdk
  namespace = "com.omega.ui"
  defaultConfig {
    minSdk = Configuration.minSdk
    targetSdk = Configuration.targetSdk
  }
  sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
  api(project(":core-ui-recyclerview"))
  api(project(":core-ui-viewpager2"))
  api(project(":core-resource"))
  api(libs.androidx.swipe)
  api(libs.androidx.constraintlayout)
  // ---- splitties ------
//  api(libs.splitties.view){
//    exclude("com.louiscad.splitties","splitties-views-android")
//    exclude("com.louiscad.splitties","splitties-views")
//  }
//  api(libs.splitties.dsl){
//    exclude("com.louiscad.splitties","splitties-views")
//    exclude("com.louiscad.splitties","splitties-views-android")
//  }
  api(project(":core-ui-splitties"))
  // ---- conductor ------
  api(libs.conductor.core)
  api(libs.conductor.autodispose)
  api(libs.conductor.archlifecycle)
  api(libs.fresco)
  api(libs.frescoGif)
  api(libs.frescoDrawable)
  api(libs.fresco.okhttp)
  api(libs.shapeofview)
  compileOnly(libs.utilcodex)
  compileOnly(libs.mmkv)
}
apply(from = "$rootDir/exclude_other_version.gradle")