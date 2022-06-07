# 蓝牙profile简介

## 蓝牙简介

- 短距离的无线通信技术
- 传统蓝牙(3.0)和低功耗蓝牙(4.0)
- 蓝牙4.0标准,包括传统蓝牙与低功耗蓝牙,是一种双模标准
- 低功耗蓝牙有点:成本与功耗降低,实时性高

## 什么是蓝牙profile

- 定义了设备实现的一种连接或者应用
- profile它们是相互依赖组合构成的,存在明显的层级关系
- 常见的`A2DP、AVRCP、HSP、HFP、OPP 、GATT...`

## A2DP

- Advanced Audio Distribution Profile **蓝牙音频传输协议**
- A2DP能提供mono或stereo的音质
- 支持A2DP的蓝牙产品一般都有支持`AVRCP`
- A2DP定义了两个角色:
    1. `Source`: 音频数据流的源
    2. `Sink`: 音频数据流的接收者

A2DP应用场景: 典型应用场景为用蓝牙立体声耳机听音乐、将立体声音乐播放器与耳机听筒相连接

## AVRCP

- Audio/Video Remote Control Profile 音频/视频远程控制规范
- AVRCP控制音频视频流的协议，包括暂停、停止、启动重放、音量控制及其它类型的远程控制操作
- AVRCP定义了两个角色:
    1. `Target`: 被控制目标设备，接收命令并按命令响应的设备，如播放/录音设备、电视，手机等
    2. `Controller`: 远程控制端设备，通过发送命令帧到目标发起传输，如车载系统、耳机，蓝牙音箱等。

AVRCP使用场景: 例如手机和蓝牙耳机，两者互相使用蓝牙连接，并且能够从耳机端以无线方式操作手机端的音乐播放：停止、播放、音量调整和前后跳曲目。

## HSP

- Headset Profile 代表耳机功能，提供手机与耳机之间通话所需的基本功能，实现了最基本的通话操作：接听电话、挂断电话、调节音量、声音在手机/蓝牙耳机之间切换。
- HSP定义了两个角色:
    1. `Audio Gate`: 音频设备输入输出网关，例如手机、电脑等
    2. `Headset`: 远程控制端设备，例如蓝牙耳机

HSP使用场景: 用蓝牙耳机可以控制手机 接听、挂断、调节音量

## HFP

- Hands-free Profile 代表免提功能，让蓝牙设备可以控制电话，如接听、挂断、拒接、拨号、免提等
- HSP仅实现了最基本的通话操作：接听电话、挂断电话、调节音量、声音在手机/蓝牙耳机之间切换
- HFP在功能上是对HSP的扩展，除了上述功能以外，还包括控制三方通话、耳机端来电显示等高级功能
- HFP定义了两个角色:
    1. Audio Gate：音频设备输入输出网关，例如手机、电脑等
    2. Hands Free：远程控制端设备，例如车载蓝牙，蓝牙音响等

HFP使用场景： 应用比较广泛的是在车载蓝牙中，手机与车载连接后可以通过汽车中控来接听挂断电话

## OPP

- Object push profile 面向对象传输协议，用于传输文件
- 由于OPP profile又细分为OPPC (client)端和OPPS(server)
  端profile，这两个profile区别在于只有client端可以发起数据传输的过程，但是附件设备与手机通信的情景中，既有手机发起数据传输请求也有设备侧发起传输请求的需要，所以要在设备中实现OPPC和OPPS两个profile。
- OPP定义了两种角色:
    1. `OPPC(client)`: OPP客户端，文件传输发起端
    2. `OPPS(server)`: OPP服务端，文件传输接收端

OPP使用场景：

典型应用场景为使用手机传送mp3给别外一个手机。

## GATT

- Generic Attribute Profile 通用属性配置文件，定义了属性类型并规定了如何使用，包括了一个数据传输和存储的框架和一些基本操作。
- GATT定义了两种角色:
    1. `Server`: 指提供数据的设备
    2. `Client`: 指通过GATT的服务器获取数据的设备

GATT使用场景: 智能穿戴设备，手环，汽车，家用电子等

## HID

- Human Interface Device Profile 人机接口设备协议 使用场景: 无线蓝牙鼠标, Wii Remotes, PlayStation 3 控制器

## ICP

- Intercom Profile 在两个设备之间建立语音连接，换句话说，把两个蓝牙牙设备变成对讲机

## LAP

- LAN Access Profile 局域网接入规范协议 本功能定义了蓝牙设备如何通过点对点协议(PPP)连接LAN的程序, 该功能实现了蓝牙设备连接已存在与LAN中的蓝牙设备的功能,
  在连接之后,该蓝牙设备即可访问上述LAN的资源

## PAN

- Personal Area Networking Profile 个域网协议, 两个或更多个 Bluetooth 设备如何构成一个即时网络

## SPP

- Serial Port Profile 串口配置协议, 定义了如何设置虚拟串行端口及如何连接两个 Bluetooth 设备