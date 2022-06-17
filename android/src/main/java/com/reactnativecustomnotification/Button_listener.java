package com.reactnativecustomnotification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class Button_listener extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
      //int id = intent.getExtras ().getInt ("id");
     // sendEvent ((ReactContext) context,"event", "String.valueOf (id)");
    Toast.makeText (context,"pressed",Toast.LENGTH_LONG).show ();
  }

  private void sendEvent(ReactContext reactContext, String eventName, String message){
    WritableMap params = Arguments.createMap ();
    params.putString ("message",message);

    reactContext
      .getJSModule (DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit (eventName,params);
  }
}
