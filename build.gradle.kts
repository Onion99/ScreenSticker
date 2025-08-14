///*
// * Designed and developed by 2022 skydoves (Jaewoong Eum)
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
plugins {
    //alias(libs.plugins.spotless)
}

buildscript {
  repositories {
    maven(uri("https://repo1.maven.org/maven2/"))
    maven(uri("https://maven.aliyun.com/repository/public"))
    maven(uri("https://maven.aliyun.com/repository/google"))
    maven(uri("https://jitpack.io"))
    mavenCentral()
    google()
    maven(uri("https://oss.sonatype.org/content/repositories/snapshots"))
  }

  dependencies {
    classpath(libs.agp)
    classpath(libs.kotlin.gradlePlugin)
    classpath(libs.hilt.plugin)
  }
}
apply(from = "$rootDir/exclude_other_version.gradle")