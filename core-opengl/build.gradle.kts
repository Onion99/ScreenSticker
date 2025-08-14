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


plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
}
apply(from = "$rootDir/android_common.gradle")
android {
  compileSdk = Configuration.compileSdk
  namespace = "com.omega.opengl"
  defaultConfig {
    minSdk = Configuration.minSdk
    externalNativeBuild {
      cmake {
        cppFlags("")
        arguments("-DANDROID_STL=c++_static")
      }
      ndk {
        abiFilters.clear()
        abiFilters+="armeabi-v7a"
        abiFilters+="arm64-v8a"
      }
    }
  }
  externalNativeBuild {
    cmake {
      path("src/main/cpp/CMakeLists.txt")
    }
  }
  sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {

}
apply(from = "$rootDir/exclude_other_version.gradle")