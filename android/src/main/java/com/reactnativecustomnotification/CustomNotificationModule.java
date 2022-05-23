package com.reactnativecustomnotification;

import static com.facebook.react.views.textinput.ReactTextInputManager.TAG;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@ReactModule(name = CustomNotificationModule.NAME)
public class CustomNotificationModule extends ReactContextBaseJavaModule {
    public static final String NAME = "CustomNotification";
  private static ReactApplicationContext reactContext;

    public CustomNotificationModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @ReactMethod
    public void createNotificationChannel(String ChannelId, String name){
      NotificationManager notificationManager = (NotificationManager) reactContext.getSystemService (Context.NOTIFICATION_SERVICE);

      if (notificationManager != null && notificationManager.getNotificationChannel (ChannelId) == null) {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel (ChannelId, name, importance);
        notificationManager.createNotificationChannel (channel);
        Log.d (TAG, "channel create");
      }else {
        Log.d (TAG, "channel is already created ");
      }
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android


  String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";

    @ReactMethod
    public void CreateInformativeNotification(String title,String description, String imageUrl){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createNotificationChannel ("channelId","informative notification");
      }

      Intent intent = new Intent (reactContext,reactContext.getClass () );
      intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,0,intent,PendingIntent.FLAG_IMMUTABLE);

      Bitmap image = null;
      try {
       URL url = new URL (imageUrl);
        image = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
      } catch (IOException e) {
        Log.d (TAG, String.valueOf (e));
      }


      RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (),R.layout.collapse_notification);
      collapseNotification.setTextViewText (R.id.title,title);
      collapseNotification.setTextViewText (R.id.description,description);

      if (image != null ){
        collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
        collapseNotification.setImageViewBitmap (R.id.avatar,image);
      }

      NotificationCompat.Builder informativeNotification = new NotificationCompat.Builder (reactContext,"channelId")
        .setSmallIcon (R.drawable.avatar)
        .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
        .setCustomContentView (collapseNotification)
        .setGroup (GROUP_KEY_WORK_EMAIL)
        .setGroupSummary (true)
        .setPriority (NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent (pendingIntent)
        .setAutoCancel (true);

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

      notificationManager.notify ((int) Math.random (),informativeNotification.build ());
    }
    @ReactMethod
  public void CreateEvaluativeNotification(){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createNotificationChannel ("ChannelId","evaluative notification");
      }

      Intent intent = new Intent (reactContext,reactContext.getClass () );
      intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,0,intent,PendingIntent.FLAG_IMMUTABLE);


      RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (),R.layout.evaluative_notification);

      NotificationCompat.Builder evaluativeNotification = new NotificationCompat.Builder (reactContext,"channelId")
        .setSmallIcon (R.drawable.avatar)
        .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
        .setCustomContentView (collapseNotification)
        .setGroup (GROUP_KEY_WORK_EMAIL)
        .setGroupSummary (true)
        .setPriority (NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent (pendingIntent)
        .setAutoCancel (true);

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

      notificationManager.notify ((int) Math.random (),evaluativeNotification.build ());

    }

    @ReactMethod
  public void CreateBigPictureNotification(String title,String description, String avatarUrl, String bigPictureUrl){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createNotificationChannel ("ChannelId","big notification notification");
      }

      RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (),R.layout.collapse_notification);
      collapseNotification.setTextViewText (R.id.title,title);
      collapseNotification.setTextViewText (R.id.description,description);

      RemoteViews ExpandedNotification = new RemoteViews (reactContext.getPackageName (),R.layout.big_picture_notification);
      ExpandedNotification.setTextViewText (R.id.title,title);
      ExpandedNotification.setTextViewText (R.id.description,description);


      Bitmap image = null;
      try {
        URL url = new URL (avatarUrl);
        image = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
      } catch (IOException e) {
        Log.d (TAG, String.valueOf (e));
      }

      Bitmap bigPicture = null;
      try {
        URL url = new URL (bigPictureUrl);
        bigPicture = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
      } catch (IOException e) {
        Log.d (TAG, String.valueOf (e));
      }

      if (image != null ){
        collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
        collapseNotification.setImageViewBitmap (R.id.avatar,image);
        ExpandedNotification.setImageViewBitmap (R.id.avatar,image);
      }

      if (bigPicture != null){
        ExpandedNotification.setViewVisibility (R.id.bigPicture,View.VISIBLE);
        ExpandedNotification.setImageViewBitmap (R.id.bigPicture,bigPicture);
      }

      Intent intent = new Intent (reactContext,reactContext.getClass () );
      intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,0,intent,PendingIntent.FLAG_IMMUTABLE);

      NotificationCompat.Builder bigPictureNotification = new NotificationCompat.Builder (reactContext,"channelId")
        .setSmallIcon (R.drawable.avatar)
        .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
        .setCustomContentView (collapseNotification)
        .setCustomBigContentView (ExpandedNotification)
        .setGroup (GROUP_KEY_WORK_EMAIL)
        .setGroupSummary (true)
        .setPriority (NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent (pendingIntent)
        .setAutoCancel (true);



      NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

      notificationManager.notify ((int) Math.random (),bigPictureNotification.build ());

    }

    @ReactMethod
  public void CreatePromotionNotification(String title,String description, String avatarUrl, String bigPictureUrl){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createNotificationChannel ("ChannelId","big picture notification");
      }

      Intent intent = new Intent (reactContext,reactContext.getClass () );
      intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,0,intent,PendingIntent.FLAG_IMMUTABLE);

      RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (),R.layout.collapse_notification);
      collapseNotification.setTextViewText (R.id.title,title);
      collapseNotification.setTextViewText (R.id.description,description);

      RemoteViews ExpandedNotification = new RemoteViews (reactContext.getPackageName (),R.layout.promotioneel_notification);
      ExpandedNotification.setTextViewText (R.id.title,title);
      ExpandedNotification.setTextViewText (R.id.description,description);
      ExpandedNotification.setTextViewText (R.id.description_product,description);
      ExpandedNotification.setTextViewText (R.id.title_product,description);

      Bitmap image = null;
      try {
        URL url = new URL (avatarUrl);
        image = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
      } catch (IOException e) {
        Log.d (TAG, String.valueOf (e));
      }

      Bitmap bigPicture = null;
      try {
        URL url = new URL (bigPictureUrl);
        bigPicture = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
      } catch (IOException e) {
        Log.d (TAG, String.valueOf (e));
      }

      if (image != null ){
        collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
        collapseNotification.setImageViewBitmap (R.id.avatar,image);
        ExpandedNotification.setImageViewBitmap (R.id.avatar,image);
      }

      if (bigPicture != null){
        ExpandedNotification.setViewVisibility (R.id.picture,View.VISIBLE);
        ExpandedNotification.setImageViewBitmap (R.id.picture,bigPicture);
      }



      NotificationCompat.Builder bigPictureNotification = new NotificationCompat.Builder (reactContext,"channelId")
        .setSmallIcon (R.drawable.avatar)
        .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
        .setCustomContentView (collapseNotification)
        .setCustomBigContentView (ExpandedNotification)
        .setGroup (GROUP_KEY_WORK_EMAIL)
        .setGroupSummary (true)
        .setPriority (NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent (pendingIntent)
        .setAutoCancel (true);

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

      notificationManager.notify ((int) Math.random (),bigPictureNotification.build ());
    }
    //public static native int nativeMultiply(int a, int b);
}
