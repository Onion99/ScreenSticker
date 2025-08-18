# ScreenSticker (屏幕贴纸)

**ScreenSticker** 是一款简洁实用的 Android 应用，它允许您像在现实世界中使用便利贴一样，在手机屏幕上创建浮动的“贴纸”。无论是记录一段文字提醒，还是贴上一张有趣的图片，它都能始终显示在其他应用之上，方便您随时查看。


<video width="800" height="450" controls autoplay>
  <source src="/doc/app_intro.mp4" type="video/mp4">
</video>

## ✨ 主要功能

-   **文本贴纸**: 创建一个可自定义颜色和内容的文本便签，并将其悬浮在屏幕上。
-   **图片贴纸**: 从您的设备中选择一张图片，作为屏幕贴纸展示。
-   **高度可定制**: 自由调整贴纸的背景颜色和文字颜色，打造个性化风格。
-   **持久化显示**: 应用关闭后，贴纸依然会保留在屏幕上。
-   **数据安全**: 即便应用进程被系统回收，重启后也能自动恢复上一次的贴纸内容，防止数据丢失。
-   **简洁易用**: 直观的用户界面，两步即可创建您的屏幕贴纸。

## 📸 截图

| 首页 |                                                                                   文本贴纸                                                                                   | 图片贴纸 |
| :---: |:------------------------------------------------------------------------------------------------------------------------------------------------------------------------:| :---: |
| ![1080X2400/pVBCREQ.png](https://tc.z.wiki/autoupload/f/DAA3COtys5EE-wZmO2gbJYyo45jBLIN4fgATAteXM7ayl5f0KlZfm6UsKj-HyTuv/20250818/yqwR/1080X2400/pVBCREQ.png?imageMogr2/thumbnail/300x) | ![415X912/sticker_text.gif](https://tc.z.wiki/autoupload/f/DAA3COtys5EE-wZmO2gbJYyo45jBLIN4fgATAteXM7ayl5f0KlZfm6UsKj-HyTuv/20250818/LXke/415X912/sticker_text.gif) | ![415X914/sticker_image.gif](https://tc.z.wiki/autoupload/f/DAA3COtys5EE-wZmO2gbJYyo45jBLIN4fgATAteXM7ayl5f0KlZfm6UsKj-HyTuv/20250818/98Jq/415X914/sticker_image.gif) |


## 💡 如何使用

1. 授予权限: 首次打开应用时，请授予“显示在其他应用上层”的权限，这是悬浮窗正常工作的前提。
2. 创建文本贴纸:
 - 在输入框中输入您想显示的文字。
 - 点击调色板图标选择背景和文本颜色。
 - 点击“显示文字”按钮，您的文本贴纸就会出现在屏幕上。
3. 创建图片贴纸:
 - 点击“选择图片”按钮。
 - 从您的相册中选择一张图片。
 - 图片贴纸将立即显示在屏幕上。
4. 关闭贴纸:双击贴纸,即可关闭



## 🛠️ 技术栈与架构

-   **语言**: [Kotlin](https://kotlinlang.org/) - 100% 使用 Kotlin 编写，遵循现代 Android 开发实践。
-   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - 用于构建声明式、响应迅速的用户界面。
-   **Route**: [Conductor](https://github.com/bluelinelabs/Conductor) - 一个基于View管理,轻量级别的页面路由
-   **数据持久化**: [MMKV](https://github.com/Tencent/MMKV) - 基于 mmap 的高性能键值对存储框架，用于在应用重启后恢复贴纸数据。

