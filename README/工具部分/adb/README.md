# adb 指令

[官方文档](https://developer.android.google.cn/studio/command-line/adb?hl=zh_cn#shellcommands)

## 分辨率相关

```bash

# 查看设备分辨率（px）
adb shell wm size

# 查像素密度（dpi）
adb shell wm density

# 修改分辨率（px）中间是x不是*
adb shell wm size 800x800

# 重置分辨率
adb shell wm size reset

```

## 发送广播
- [-a <ACTION>]
- [-d <DATA_URI>]
- [-t <MIME_TYPE>] 
- [-c <CATEGORY> [-c <CATEGORY>] ...] 
- [-e|--es <EXTRA_KEY> <EXTRA_STRING_VALUE> ...] 
- [--ez <EXTRA_KEY> <EXTRA_BOOLEAN_VALUE> ...] 
- [-e|--ei <EXTRA_KEY> <EXTRA_INT_VALUE> ...] 
- [-n <COMPONENT>]
- [-f <FLAGS>] [<URI>]

```bash
adb shell am broadcast -a android.bluetooth.adapter.action.STATE_CHANGED --ei android.bluetooth.adapter.extra.STATE 3

adb shell am broadcast -a com.autonavi.amapauto.AUTONAVI_STANDARD_BROADCAST_SEND --ei com.autonavi.amapauto.KEY_TYPE 10019 --ei com.autonavi.amapauto.EXTRA_STATE 8

adb shell am broadcast -a AUTONAVI_STANDARD_BROADCAST_SEND --ei KEY_TYPE 10019 --ei EXTRA_STATE 8

adb shell am start -a com.bmi.launcher/.ui.activity.MainActivity


# 查询 adb shell 所有命令 
adb shell ls /system/bin

adb shell am start -a com.bmi.launcher/.ui.activity.MainActivity -- display 1

# 系统Car设置
adb shell am start  -n com.android.car.settings/com.android.car.settings.Settings

adb shell am start -n com.debug.loggerui/.MainActivity

```

## 查看APK是32位还是64位

```
ARM 32位对应的是armv7架构、armv6架构、armv5架构

ARM 64位是armv8架构

常用的abi:

armeabi: armv5架构和armv6架构 （32位）

armeabi-v7a:armv7架构 （32位）

x86:x86架构 （32位）

armeabi-v8a:armv8架构 （64位）

x86_64:x86_64架构 （64位）
```



[getprop，dumpsys](https://blog.csdn.net/qq_28171461/article/details/60964563)

[命令行工具](https://developer.android.google.cn/studio/command-line?hl=zh-cn)