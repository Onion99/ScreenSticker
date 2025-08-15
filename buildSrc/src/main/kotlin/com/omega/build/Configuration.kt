package com.omega.build

object Configuration {
  const val app = "ScreenSticker"
  const val compileSdk = 36
  const val targetSdk = 36
  const val minSdk = 26
  private const val majorVersion = 0
  private const val minorVersion = 0
  private const val patchVersion = 1
  const val versionName = "$majorVersion.$minorVersion.$patchVersion"
  val versionCode = "$majorVersion$minorVersion$patchVersion".toInt()
  const val nameSpace= "com.omega.sun"
  const val debugApplicationId= "com.omega.sun"
  const val debugSignPassWord = "nova9999"
  const val debugSignAlias = "nova"
  const val releaseApplicationId = "com.omega.star"
  const val releaseSignPassWord = "nova9999"
  const val releaseSignAlias = "nova"
}
