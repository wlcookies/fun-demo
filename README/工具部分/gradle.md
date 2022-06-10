# gradle

## 学习资料参见

- [Android apk签名详解——AS签名、获取签名信息、系统签名、命令行签名](https://blog.csdn.net/jb_home/article/details/121342438)

## gradle配置签名信息

```groovy
android {
    signingConfigs {
        debug {
            storeFile file("${getRootDir()}\\android_aosp.jks")
            storePassword ''
            keyAlias ''
            keyPassword ''
        }

        release {
            storeFile file("${getRootDir()}\\android_aosp.jks")
            storePassword ''
            keyAlias ''
            keyPassword ''
            v1SigningEnabled true
            v2SigningEnabled true
        }
    }

    buildTypes {
        release {
            // 开启混淆
            minifyEnabled true
            //开启资源压缩
            shrinkResources true
            // 开启Zip压缩优化
            zipAlignEnabled true
            // 混淆文件
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            // 使用的签名配置
            signingConfig signingConfigs.release
        }
        debug {
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    // 配置 release apk 的输出位置，项目根目录 release 文件夹下 
    applicationVariants.all {
        variant ->
            variant.outputs.all {
                if (variant.buildType.name == 'release') {
                    getPackageApplication().outputDirectory = new File(project.rootDir.absolutePath + File.separator + "release")
                    outputFileName = "xxxxx.apk"
                }
            }
    }
}
```

## jks 与 keystore 区别

## 获取签名文件的 MD5 和 sha256 的值

```shell
keytool -v -list -keystore xxxx.jks
```


