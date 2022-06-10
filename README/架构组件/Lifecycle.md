# Lifecycle

## 简介

- 可以感知响应(`Activity、Service、Fragment、Application`)的生命周期变化
- 常见的某些功能需要与组件的生命周期相互联系，`Lifecycle`可以将逻辑从组件中抽离出来

## 使用

### 库版本

[最新版本](https://developer.android.google.cn/jetpack/androidx/releases/lifecycle)

### 实现观察者

#### 方式1 implements DefaultLifecycleObserver

#### 方式2 implements LifecycleEventObserver

### Application

```groovy
implementation "androidx.lifecycle:lifecycle-process:$lifecycle_version"
```

```java
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 绑定观察者
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new xxx());
    }
}
```

### Service

```groovy
implementation "androidx.lifecycle:lifecycle-service:$lifecycle_version"
```

```java
// 继承至LifecycleService
public class MyService extends LifecycleService {

    @Override
    public void onCreate() {
        super.onCreate();
        // 绑定观察者
        getLifecycle().addObserver(new xxx());
    }
}
```

### Activity/Fragment

```java
public class Xxxxx extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 绑定观察者
        getLifecycle().addObserver(new Xxxx());
    }
}
```
