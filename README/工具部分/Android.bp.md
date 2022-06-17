# Android.bp

- android 7.0 开始引用bp，之前都是mk来编译源码的
- bp代替mk
- Android 7.0 引入`ninja`和`kati`
- Android 8.0 使用`Android.bp`来替换`Android.mk`，引入`Soong`
- Android 9.0 强制使用`Android.bp`
  
- 经过`Kati`将`Android.mk`转换成`ninja`格式的文件
- 经过`Blueprint`+`Soong`将`Android.bp`转换成`ninja`格式的文件
- 经过`androidmk`将`Android.mk`转换成`Android.bp`

## Android编译工具

### Ninja

`ninja`是一个编译框架，会根据相应的`ninja`格式的配置文件进行编译，可是`ninja`文件通常不会手动修改，而是经过将`Android.bp`文件转换成`ninja`格式文件来编译

### Android.bp

`Android.bp`的出现就是为了替换`Android.mk`文件。`bp`跟`mk`文件不一样，它是纯粹的配置，没有分支、循环等流程控制，不能作算数逻辑运算。若是须要控制逻辑，那么只能经过`Go`语言编写。框架

### Soong

`Soong`相似于以前的`Makefile`编译系统的核心，负责提供`Android.bp`语义解析，并将之转换成`Ninja`文件。`Soong`还会编译生成一个`androidmk命令`，用于将`Android.mk`文件转换为`Android.bp`文件，不过这个转换功能仅限于没有分支、循环等流程控制的`Android.mk`才有效。

### Blueprint

`Blueprint`是生成、解析`Android.bp`的工具，是`Soong`的一部分。`Soong`负责`Android`编译而设计的工具，而`Blueprint`只是解析文件格式，`Soong`解析内容的具体含义。`Blueprint`和`Soong`都是由`Golang`写的项目，从Android 7.0，prebuilts/go/目录下新增Golang所需的运行环境，在编译时使用。

### Kati

`kati`是专为Android开发的一个基于`Golang`和`C++`的工具，主要功能是把Android中的`Android.mk`文件转换成`Ninja`文件。代码路径是build/kati/，编译后的产物是ckati。

## Android.bp 语法入门

[参考资料](https://blog.csdn.net/liujun3512159/article/details/124601811) 