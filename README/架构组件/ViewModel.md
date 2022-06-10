# ViewModel

注重生命周期的方式存储和管理界面相关的数据

- extends ViewModel
- 其中主要依赖`LiveData`存储数据
- **可以在Fragment之间共享，在各个Fragment中，通过`new ViewModelProvider(requireActivity())`其范围限定为Activity**

- [将加载器替换成ViewModel](https://developer.android.google.cn/topic/libraries/architecture/viewmodel#loaders)
- [将协程与 ViewModel 一起使用](https://developer.android.google.cn/topic/libraries/architecture/viewmodel#coroutines)