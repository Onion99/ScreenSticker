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
  alias(libs.plugins.compose.compiler)
}
apply(from = "$rootDir/android_common.gradle")
android {
  compileSdk = Configuration.compileSdk
  namespace = "com.omega.ui"
  defaultConfig {
    minSdk = Configuration.minSdk
  }
  sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
  // ---- module ------
  api(projects.coreResource)
  api(projects.coreUiSplitties)
  // ---- view ------
  api(libs.androidx.swipe)
  api(libs.androidx.constraintlayout)
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
  // ---- compose ------
  val composeBom = platform(libs.androidx.compose.bom)
  api(composeBom)
  api(libs.androidx.compose.runtime)
  api(libs.androidx.compose.foundation)
  api(libs.androidx.compose.foundation.layout)
  api(libs.androidx.compose.ui)
  api(libs.androidx.compose.ui.util)
  api(libs.androidx.compose.material3)
  api(libs.androidx.compose.animation)
  api(libs.androidx.compose.ui.tooling.preview)
}
apply(from = "$rootDir/exclude_other_version.gradle")