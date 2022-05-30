package com.reactnativecustomnotification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  public static ReactApplicationContext reactContext;
  @Override
  public void onMessageReceived(@NonNull RemoteMessage message) {
    //super.onMessageReceived (message);
    CustomNotificationModule customNotificationModule= new CustomNotificationModule (reactContext) ;
    if (message.getNotification () != null){
      //customNotificationModule.CreateBigPictureNotification (message.getNotification ().getTitle (),message.getNotification ().getBody (),"https://cdn-icons-png.flaticon.com/512/147/147144.png","https://cdn-icons-png.flaticon.com/512/147/147144.png");

     NotificationCompat.Builder builder = new NotificationCompat.Builder(reactContext, "CHANNEL_ID")
        .setContentTitle(message.getNotification ().getTitle ())
        .setContentText(message.getNotification ().getBody ())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(reactContext);

// notificationId is a unique int for each notification that you must define
      notificationManager.notify(Integer.parseInt ("notificationId"), builder.build());


    }

  }
}

