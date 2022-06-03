# ViewBinding

代替 **findViewById**，该库会为每个xml文件生成一个绑定类，类名就是xml文件名驼峰后加Binding

## 配置

```gradle
android {
    viewBinding {
        enabled = true
    }
}
```

## viewBindingIgnore忽略某个布局

```xml
<LinearLayout
    tools:viewBindingIgnore="true" >
</LinearLayout>    
```

## Activity中使用

```java

// 视图绑定类
private ResultProfileBinding binding;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 实例化
    binding = ResultProfileBinding.inflate(getLayoutInflater());
    // 获取根视图
    View view = binding.getRoot();
    // 填充
    setContentView(view);
}
```

## Fragment中使用

<font color = 'red'>Fragment 的存在时间比其视图长，请务必在 Fragment 的 onDestroyView() 方法中清除对绑定类实例的所有引用。</font>

```java
private ResultProfileBinding binding;

@Override
public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
    binding = ResultProfileBinding.inflate(inflater, container, false);
    View view = binding.getRoot();
    return view;
}

@Override
public void onDestroyView() {
    super.onDestroyView();
    // 解除引用
    binding = null;
}    
```