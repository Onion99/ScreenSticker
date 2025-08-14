# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradleold.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#打印混淆信息
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-keepattributes SourceFile,LineNumberTable
#-keepattributes Exceptions, Signature, InnerClasses, EnclosingMethod
-keepclassmembers class androidx.fragment.app.Fragment { *; }
#保留di，reflect相关
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留banner类混淆
-keep class androidx.recyclerview.** {*;}
-keep class androidx.viewpager2.** {*;}

# 保留所有的Model类
-keep public class com.nova.model.** { *; }

#移动到跟目录/
-repackageclasses ''
-allowaccessmodification
-useuniqueclassmembernames
-keeppackagenames doNotKeepAThing

# ------------------------------------- 数字 DNA begin -------------------------------------
-keep class cn.shuzilm.core.Main {
    public *;
}
-keep class cn.shuzilm.core.Listener {
    public *;
}
-keepclasseswithmembernames class cn.shuzilm.core.Main {
    native <methods>;
}
-keep class cn.shuzilm.core.** {*;}

#混淆资源名
-adaptresourcefilenames **.propertie,**.version,**.gif,**.jpg
#-adaptresourcefilecontents **.properties,**.version,META-INF/MANIFEST.MF

# 为什么不混淆class_spec？
#-whyareyoukeeping class com.gmlive.common.dynamicdomain.DynamicDomain { *; }

#af混淆处理
-keep class com.appsflyer.** { *; }
-keep public class com.android.installreferrer.** { *; }

# -------------------------------------- anim --------------------------------------
-keep class org.cocos2dx.**{*;}
-dontwarn org.cocos2dx.**
-keep class com.opensource.svgaplayer.**{*;}
-dontwarn com.opensource.svgaplayer.**
-keep class eu.davidea.flexibleadapter.**{*;}
-dontwarn eu.davidea.flexibleadapter.**
# ------------------------------------------- Mars------------------------------------------
-keep class com.tencent.mars.xlog.** { *; }
-dontwarn com.tencent.mars.xlog.**
# ------------------------------------------- Mars------------------------------------------
-keep class com.nova.ui.splitties.** { *; }
-dontwarn com.nova.ui.splitties.**
-keep class com.louiscad.splitties.** { *; }
-dontwarn com.louiscad.splitties.**
# ------------------------------------------- Fresco------------------------------------------
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}

# Keep native methods
-keepclassmembers class com.facebook.** {
    native <methods>;
}

# Do not strip SoLoader class and init method
-keep public class com.facebook.soloader.SoLoader {
    public static void init(android.content.Context, int);
}
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
# ------------------------------------------- exoplayer2------------------------------------------
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**
# ------------------------------------------- For BannerViewPager------------------------------------------
-keep class androidx.recyclerview.widget.**{*;}
-dontwarn androidx.recyclerview.widget.**
-keep class com.nova.recyclerview.**{*;}
-dontwarn com.nova.recyclerview.**
-keep class androidx.viewpager2.widget.**{*;}
-dontwarn androidx.viewpager2.widget.**
-keep class androidx.viewpager.widget.**{*;}
-dontwarn androidx.viewpager.widget.**
-keep class com.nova.viewpager2.**{*;}
-dontwarn com.nova.viewpager2.**
# ------------------------------------------- exoplayer2------------------------------------------
-keep class org.jzvd.jzvideo.** { *; }
-dontwarn org.jzvd.jzvideo.**
-keep class cn.jzvd.** { *; }
-dontwarn cn.jzvd.**
# ------------------------------------------- vap------------------------------------------
-keep class com.tencent.qgame.animplayer.** { *; }
-dontwarn com.tencent.qgame.animplayer.**
# ------------------------------------------- controller ------------------------------------------
-keep class com.nova.sun.ui.controller.** { *; }
-dontwarn com.nova.sun.ui.controller.**
# ------------------------------------------- bannerview ------------------------------------------
-keep class com.zhpan.bannerview.** { *; }
-dontwarn com.zhpan.bannerview.**
