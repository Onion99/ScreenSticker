cmake_minimum_required(VERSION 3.4.1)

##官方标准配置
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall")

# ANDROID_PLATFORM_LEVEL=18
add_definitions("-DDYNAMIC_ES3")
# 设置OPENGL库 版本
set(OPENGL_LIB GLESv3)


file(GLOB src-files
        ${CMAKE_SOURCE_DIR}/*.cpp)

# 添加依赖库
add_library(
        # 设置库名
        native-color
        # 设置可以共享
        SHARED
        # 提供文件相对路径,Provides a relative path to your source file(s).
        ${src-files}
)

target_link_libraries(
        # 指定目标库
        native-color
        # 链接相关库 OPENGL3
        ${OPENGL_LIB}
        # 链接相关库 android
        android
        # 链接相关库 egl
        EGL
        # 链接相关库 log
        log
        m)