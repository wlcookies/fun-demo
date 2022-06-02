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
adb shell am broadcast -a AUTONAVI_STANDARD_BROADCAST_SEND --ei KEY_TYPE 10019 --ei EXTRA_STATE 9
```