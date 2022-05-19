package com.reactnativecustomnotification;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = CustomNotificationModule.NAME)
public class CustomNotificationModule extends ReactContextBaseJavaModule {
    public static final String NAME = "CustomNotification";

    public CustomNotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void multiply(int a, int b, Promise promise) {
        promise.resolve(a + b);
    }

    @ReactMethod
    public int test(int x, int y){
      return (x+y)*2;
    }

    //public static native int nativeMultiply(int a, int b);
}
