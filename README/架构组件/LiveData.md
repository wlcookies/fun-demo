# LiveDate

## 简介

- 可观察的数据存储器类
- 具有生命周期感知能力(Activity、Fragment 或 Service)
- 确保仅更新处于活跃生命周期状态下的应用组件观察者(观察者处于STARTED、RESUMED)
- 观察者从非活跃状态更改为活跃状态时也会收到更新
- 结合ViewModel使用

## 优势

- 确保界面符合数据状态(不会因为设备配置发生改变导致数据丢失)
- 不会发生内存泄露(观察者会绑定在Lifecycle对象上,并在其关联的生命周期销毁后自我清理)
- 不会因为Activity停止而崩溃(观察者的生命周期处于非活跃状态（如返回栈中的 Activity）,则它不会接收任何 LiveData 事件)
- 不再需要手动处理生命周期
- 数据始终保存最新状态
- 共享资源(使用单例模式扩展 LiveData 对象以封装系统服务, 以便在应用中共享它们, LiveData 对象连接到系统服务一次, 然后需要相应资源的任何观察者只需观察 LiveData
  对象)

## 使用

1. 通常在`ViewModel`中，创建
2. 定义`androidx.lifecycle.Observer`在其`onChanged()`方法中，处理业务逻辑
3. 使用`LiveData`的`observe()`，将`Observer`对象附加到`LiveData`上
4. <font color="red">使用observeForever，记得要removeObserver(Observer) </font>

### 创建LiveDate对象

```java
public class XxxViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _connectState = new MutableLiveData<>();
    public LiveData<Boolean> connectState = _connectState;

    public void setConnectState(boolean isConnect) {
        _connectState.setValue(isConnect);
    }
}
```

### 观察LiveDate对象

1. 大多数情况下，组件在其`onCreate()`中开始观察

### 更新LiveDate对象

1. `setValue(T)` —— 主线程
2. `postValue(T)` —— 工作线程

## 扩展LiveData

- `extend LiveData`
- `onActive()` 当LiveData对象具有活跃观察者时，会调用，意味着需要在这个方法中更新值
- `onInactive()` 当LiveData对象没有任何活跃观察者时，会调用
- `setValue(T)` 更新值时调用

备注：
<font color="red">可以通过单例的模式，扩展LiveData，这样可以使多个组件共享一个LiveData</font>

### 蓝牙状态的LiveData

```java
public class BluetoothStateLiveData extends LiveData<Integer> {

    @IntDef({
            BluetoothState.UNKNOWN,
            BluetoothState.DISABLED,
            BluetoothState.ENABLED,
    })
    public @interface BluetoothState {
        /**
         * 当前设备不支持
         */
        int UNKNOWN = 0;
        /**
         * 蓝牙不可用
         */
        int DISABLED = 1;
        /**
         * 蓝牙可用
         */
        int ENABLED = 2;
    }

    // 蓝牙适配器
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Context mContext;
    private final IntentFilter mIntentFilter = new IntentFilter();

    // 广播接收器，蓝牙状态改变
    private final BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateState();
        }
    };

    /**
     * 初始化
     */
    @MainThread
    public BluetoothStateLiveData(Context context) {
        mContext = context;
        mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    /**
     * 活跃状态，注册广播接收器
     */
    @Override
    protected void onActive() {
        if (mBluetoothAdapter != null) {
            updateState();
            mContext.registerReceiver(mBluetoothStateReceiver, mIntentFilter);
        }
    }

    /**
     * 非活跃，注销广播接收器
     */
    @Override
    protected void onInactive() {
        if (mBluetoothAdapter != null) {
            mContext.unregisterReceiver(mBluetoothStateReceiver);
        }
    }

    /**
     * 更新值
     */
    private void updateState() {
        @BluetoothState int state = BluetoothState.UNKNOWN;
        if (mBluetoothAdapter != null) {
            state = mBluetoothAdapter.isEnabled() ? BluetoothState.ENABLED
                    : BluetoothState.DISABLED;
        }
        if (getValue() == null || state != getValue()) {
            setValue(state);
        }
    }
}
```

## 转换

### Transformations.map()

`map` 是将现有的`LiveData`，存储的值转换

```java
public class Xxxx {
    LiveData<String> map = Transformations.map(new BluetoothStateLiveData(requireActivity()), new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "当前蓝牙状态" + input;
        }
    });
}
```

### Transformations.switchMap()

`switchMap`

```java
public class Xxx {
    // 获取用户信息，通过id
    private LiveData<User> getUser(String id) {
    }

    // 用户ID
    LiveData<String> userId = new Xxx();
    // 把 LiveData userId，中的值，调用函数，在函数中，调用getUser，返回LiveData
    LiveData<User> user = Transformations.switchMap(userId, id -> getUser(id));
}
```

## 创建新的转换_合并多个LiveData源(MediatorLiveData)

1. `MediatorLiveData` 是 `LiveData` 子类
2. 可以用于合并多个 `LiveData` 源
3. 只要任何一个原始的 `LiveDate` 源对象改变了，就会触发 `MediatorLiveData`

示例代码来源于Android 10 源码
```java
public class TelecomActivityViewModel extends AndroidViewModel {
    
    private static final String TAG = "CD.TelecomActivityViewModel";
    /** A constant which indicates that there's no Bluetooth error. */
    public static final String NO_BT_ERROR = "NO_ERROR";

    private final Context mApplicationContext;
    private final LiveData<String> mErrorStringLiveData;
    private final MutableLiveData<Integer> mDialerAppStateLiveData;

    /**
     * App state indicates if bluetooth is connected or it should just show the content fragments.
     */
    @IntDef({DialerAppState.DEFAULT, DialerAppState.BLUETOOTH_ERROR,
            DialerAppState.EMERGENCY_DIALPAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialerAppState {
        int DEFAULT = 0;
        int BLUETOOTH_ERROR = 1;
        int EMERGENCY_DIALPAD = 2;
    }

    public TelecomActivityViewModel(Application application) {
        super(application);
        mApplicationContext = application.getApplicationContext();

        if (BluetoothAdapter.getDefaultAdapter() == null) {
            MutableLiveData<String> bluetoothUnavailableLiveData = new MutableLiveData<>();
            bluetoothUnavailableLiveData.setValue(
                    mApplicationContext.getString(R.string.bluetooth_unavailable));
            mErrorStringLiveData = bluetoothUnavailableLiveData;
        } else {
            UiBluetoothMonitor uiBluetoothMonitor = UiBluetoothMonitor.get();
            mErrorStringLiveData = new ErrorStringLiveData(
                    mApplicationContext,
                    uiBluetoothMonitor.getHfpStateLiveData(),
                    uiBluetoothMonitor.getPairListLiveData(),
                    uiBluetoothMonitor.getBluetoothStateLiveData());
        }

        mDialerAppStateLiveData = new DialerAppStateLiveData(mErrorStringLiveData);
    }

    public MutableLiveData<Integer> getDialerAppState() {
        return mDialerAppStateLiveData;
    }

    /**
     * Returns a LiveData which provides the warning string based on Bluetooth states. Returns
     * {@link #NO_BT_ERROR} if there's no error.
     */
    public LiveData<String> getErrorMessage() {
        return mErrorStringLiveData;
    }

    private static class DialerAppStateLiveData extends MediatorLiveData<Integer> {
        private final LiveData<String> mErrorStringLiveData;

        private DialerAppStateLiveData(LiveData<String> errorStringLiveData) {
            this.mErrorStringLiveData = errorStringLiveData;
            setValue(DialerAppState.DEFAULT);

            addSource(mErrorStringLiveData, errorMsg -> updateDialerAppState());
        }

        private void updateDialerAppState() {
            L.d(TAG, "updateDialerAppState, error: %s", mErrorStringLiveData.getValue());

            // If bluetooth is not connected, user can make an emergency call. So show the in
            // call fragment no matter if bluetooth is connected or not.
            // Bluetooth error
            if (!NO_BT_ERROR.equals(mErrorStringLiveData.getValue())) {
                // Currently bluetooth is not connected, stay on the emergency dial pad page.
                if (getValue() == DialerAppState.EMERGENCY_DIALPAD) {
                    return;
                }
                setValue(DialerAppState.BLUETOOTH_ERROR);
                return;
            }

            // Bluetooth connected.
            setValue(DialerAppState.DEFAULT);
        }

        @Override
        public void setValue(@DialerAppState Integer newValue) {
            // Only set value and notify observers when the value changes.
            if (getValue() != newValue) {
                super.setValue(newValue);
            }
        }
    }

    private static class ErrorStringLiveData extends MediatorLiveData<String> {
        private LiveData<Integer> mHfpStateLiveData;
        private LiveData<Set<BluetoothDevice>> mPairedListLiveData;
        private LiveData<Integer> mBluetoothStateLiveData;

        private Context mContext;

        ErrorStringLiveData(Context context,
                            BluetoothHfpStateLiveData hfpStateLiveData,
                            BluetoothPairListLiveData pairListLiveData,
                            BluetoothStateLiveData bluetoothStateLiveData) {
            mContext = context;
            mHfpStateLiveData = hfpStateLiveData;
            mPairedListLiveData = pairListLiveData;
            mBluetoothStateLiveData = bluetoothStateLiveData;
            setValue(NO_BT_ERROR);

            addSource(hfpStateLiveData, this::onHfpStateChanged);
            addSource(pairListLiveData, this::onPairListChanged);
            addSource(bluetoothStateLiveData, this::onBluetoothStateChanged);
        }

        private void onHfpStateChanged(Integer state) {
            update();
        }

        private void onPairListChanged(Set<BluetoothDevice> pairedDevices) {
            update();
        }

        private void onBluetoothStateChanged(Integer state) {
            update();
        }

        private void update() {
            boolean isBluetoothEnabled = isBluetoothEnabled();
            boolean hasPairedDevices = hasPairedDevices();
            boolean isHfpConnected = isHfpConnected();
            L.d(TAG, "Update error string."
                            + " isBluetoothEnabled: %s"
                            + " hasPairedDevices: %s"
                            + " isHfpConnected: %s",
                    isBluetoothEnabled,
                    hasPairedDevices,
                    isHfpConnected);
            if (!isBluetoothEnabled) {
                setValue(mContext.getString(R.string.bluetooth_disabled));
            } else if (!hasPairedDevices) {
                setValue(mContext.getString(R.string.bluetooth_unpaired));
            } else if (!isHfpConnected) {
                setValue(mContext.getString(R.string.no_hfp));
            } else {
                if (!NO_BT_ERROR.equals(getValue())) {
                    setValue(NO_BT_ERROR);
                }
            }
        }

        private boolean isHfpConnected() {
            Integer hfpState = mHfpStateLiveData.getValue();
            return hfpState == null || hfpState == BluetoothProfile.STATE_CONNECTED;
        }

        private boolean isBluetoothEnabled() {
            Integer bluetoothState = mBluetoothStateLiveData.getValue();
            return bluetoothState == null
                    || bluetoothState != BluetoothStateLiveData.BluetoothState.DISABLED;
        }

        private boolean hasPairedDevices() {
            Set<BluetoothDevice> pairedDevices = mPairedListLiveData.getValue();
            return pairedDevices == null || !pairedDevices.isEmpty();
        }
    }
}
```

## 与Room一起使用

[文档](https://developer.android.google.cn/training/data-storage/room)

## 与协程一起使用

[文档](https://developer.android.google.cn/topic/libraries/architecture/coroutines)


