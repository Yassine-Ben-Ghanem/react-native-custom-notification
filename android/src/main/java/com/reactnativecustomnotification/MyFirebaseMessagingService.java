package com.reactnativecustomnotification;

import static com.reactnativecustomnotification.CustomNotificationModule.reactContext;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.net.ssl.KeyManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


  @Override
  public void onMessageReceived(@NonNull RemoteMessage message) {
    String title = message.getNotification ().getTitle ();
    String text = message.getNotification ().getBody ();
    CustomNotificationModule customNotificationModule = new CustomNotificationModule (reactContext);
    customNotificationModule.CreateInformativeNotification (title,text, "https://i.stack.imgur.com/ILTQq.png","https://i.stack.imgur.com/ILTQq.png");

    super.onMessageReceived (message);
 }

  @Override
  public void onNewToken(@NonNull String token) {
    super.onNewToken (token);
    Log.e ("Token","onNewToken"+token);
  }
}

