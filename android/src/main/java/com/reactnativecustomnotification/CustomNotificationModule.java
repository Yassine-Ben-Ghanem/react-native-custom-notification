package com.reactnativecustomnotification;

import static com.facebook.react.views.textinput.ReactTextInputManager.TAG;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@ReactModule(name = CustomNotificationModule.NAME)
public class CustomNotificationModule extends ReactContextBaseJavaModule {
  public static final String NAME = "CustomNotification";
  public static ReactApplicationContext reactContext;

  //private Button_listener button_listener = new Button_listener ();

  BroadcastReceiver br = new Button_listener ();
  IntentFilter filter = new IntentFilter (ConnectivityManager.CONNECTIVITY_ACTION);


  String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";



  public CustomNotificationModule(ReactApplicationContext context) {
    super (context);
    reactContext = context;
    FirebaseMessaging.getInstance().getToken()
      .addOnCompleteListener(new OnCompleteListener<String>() {
        @Override
        public void onComplete(@NonNull Task<String> task) {
          if (!task.isSuccessful()) {
            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            return;
          }

          // Get new FCM registration token
          String token = task.getResult();

          // Log and toast
          String msg = "message";
          Log.d(TAG, token);
        }
      });
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  private void sendEvent(ReactContext reactContext, String eventName, String message) {
    WritableMap params = Arguments.createMap ();
    params.putString ("message", message);

    reactContext
      .getJSModule (DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit (eventName, params);
  }


  @RequiresApi(api = Build.VERSION_CODES.O)
  public void createNotificationChannel(String channelId, String name) {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    if (notificationManager != null && notificationManager.getNotificationChannel (channelId) == null) {
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel channel = new NotificationChannel (channelId, name, importance);
      notificationManager.createNotificationChannel (channel);
      Log.d (TAG, "channel create");
    } else {
      Log.d (TAG, "channel is already created ");
    }
  }

  @ReactMethod
  public void CreateInformativeNotification(String title, String description, String imageUrl,String actionNotification) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
      createNotificationChannel ("channelId", "informative notification");
    }

    Intent intent = new Intent (Intent.ACTION_VIEW);
    intent.setData (Uri.parse (actionNotification));
    PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,25,intent,0);

    Bitmap image = null;
    try {
      URL url = new URL (imageUrl);
      image = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
    } catch (IOException e) {
      Log.d (TAG, String.valueOf (e));
    }

    RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (), R.layout.collapse_notification);
    collapseNotification.setTextViewText (R.id.title, title);
    collapseNotification.setTextViewText (R.id.description, description);

    if (image != null) {
      collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
      collapseNotification.setImageViewBitmap (R.id.avatar, image);
    }

    NotificationCompat.Builder informativeNotification = new NotificationCompat.Builder (reactContext, "channelId")
      .setSmallIcon (R.drawable.avatar)
      .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
      .setContentIntent (pendingIntent)
      .setCustomContentView (collapseNotification)
      .setGroup (GROUP_KEY_WORK_EMAIL)
      .setGroupSummary (true)
      .setPriority (NotificationCompat.PRIORITY_DEFAULT)
      .setNumber (1)
      .setBadgeIconType (NotificationCompat.BADGE_ICON_LARGE)
      .setAutoCancel (true);

   // NotificationManager notificationManager = (NotificationManager) reactContext.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    /*String token = FirebaseMessaging.getInstance ().getToken ().getResult ();

    Log.e (TAG, String.valueOf (token));
*/
    notificationManager.notify ((int) 1, informativeNotification.build ());
  }

  @ReactMethod
  public String getDeviceToken(){
    /*Task<String> token = FirebaseMessaging.getInstance ().getToken ()
      .addOnCompleteListener (new OnCompleteListener<String> () {
        @Override
        public void onComplete(@NonNull Task<String> task) {
          if (!task.isSuccessful ()){
            Log.w (TAG,"Fetching FCM registration token failed",task.getException ());
            return;
          }
          String token = task.getResult ();
          Log.d (TAG,token);
        }
      });*/
    return FirebaseMessaging.getInstance ().getToken ().getResult ();
  }

  @ReactMethod
  public void CreateAnimationNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel ("ChannelId", "evaluative notification");
    }

    Intent intent = new Intent (reactContext, reactContext.getClass ());
    intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity (reactContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);


    RemoteViews ExpandNotification = new RemoteViews (reactContext.getPackageName (), R.layout.caroussel_layout_notification);
    RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (), R.layout.collapse_notification);

    NotificationCompat.Builder evaluativeNotification = new NotificationCompat.Builder (reactContext, "channelId")
      .setSmallIcon (R.drawable.avatar)
      .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
      .setCustomContentView (collapseNotification)
      .setCustomBigContentView (ExpandNotification)
      .setGroup (GROUP_KEY_WORK_EMAIL)
      .setGroupSummary (true)
      .setBadgeIconType (NotificationCompat.BADGE_ICON_LARGE)
      .setPriority (NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent (pendingIntent)
      .setNumber (1)
      .setAutoCancel (true);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    notificationManager.notify ((int) Math.random (), evaluativeNotification.build ());

  }

  @ReactMethod
  public void CreateBigPictureNotification(String title, String description, String avatarUrl, String bigPictureUrl,String actionNotification) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel ("ChannelId", "big notification notification");
    }
    RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (), R.layout.collapse_notification);
    collapseNotification.setTextViewText (R.id.title, title);
    collapseNotification.setTextViewText (R.id.description, description);

    RemoteViews ExpandedNotification = new RemoteViews (reactContext.getPackageName (), R.layout.big_picture_notification);
    ExpandedNotification.setTextViewText (R.id.title, title);
    ExpandedNotification.setTextViewText (R.id.description, description);

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

    if (image != null) {
      collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
      collapseNotification.setImageViewBitmap (R.id.avatar, image);
      ExpandedNotification.setImageViewBitmap (R.id.avatar, image);
    }

    if (bigPicture != null) {
      ExpandedNotification.setViewVisibility (R.id.bigPicture, View.VISIBLE);
      ExpandedNotification.setImageViewBitmap (R.id.bigPicture, bigPicture);
    }

    Intent intent = new Intent (Intent.ACTION_VIEW);
    intent.setData (Uri.parse (actionNotification));
    PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,25,intent,PendingIntent.FLAG_IMMUTABLE);

    NotificationCompat.Builder bigPictureNotification = new NotificationCompat.Builder (reactContext, "channelId")
      .setSmallIcon (R.drawable.avatar)
      .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
      .setCustomContentView (collapseNotification)
      .setCustomBigContentView (ExpandedNotification)
      .setGroup (GROUP_KEY_WORK_EMAIL)
      .setGroupSummary (true)
      .setPriority (NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent (pendingIntent)
      .setBadgeIconType (NotificationCompat.BADGE_ICON_LARGE)
      .setNumber (1)
      .setAutoCancel (true);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    notificationManager.notify ((int) Math.random (), bigPictureNotification.build ());
  }

  @ReactMethod
  public void CreateProductNotification(String title, String description, String avatarUrl, String bigPictureUrl,String productTitle,String productDescription,String actionNotification) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel ("ChannelId", "big picture notification");
    }

    Intent intent = new Intent (Intent.ACTION_VIEW);
    intent.setData (Uri.parse (actionNotification));
    PendingIntent pendingIntent = PendingIntent.getActivity (reactContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);

    RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (), R.layout.collapse_notification);
    collapseNotification.setTextViewText (R.id.title, title);
    collapseNotification.setTextViewText (R.id.description, description);

    RemoteViews ExpandedNotification = new RemoteViews (reactContext.getPackageName (), R.layout.promotioneel_notification);
    ExpandedNotification.setTextViewText (R.id.title, title);
    ExpandedNotification.setTextViewText (R.id.description, description);
    ExpandedNotification.setTextViewText (R.id.description_product, productDescription);
    ExpandedNotification.setTextViewText (R.id.title_product, productTitle);

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

    if (image != null) {
      collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
      collapseNotification.setImageViewBitmap (R.id.avatar, image);
      ExpandedNotification.setImageViewBitmap (R.id.avatar, image);
    }

    if (bigPicture != null) {
      ExpandedNotification.setViewVisibility (R.id.picture, View.VISIBLE);
      ExpandedNotification.setImageViewBitmap (R.id.picture, bigPicture);
    }


    NotificationCompat.Builder bigPictureNotification = new NotificationCompat.Builder (reactContext, "channelId")
      .setSmallIcon (R.drawable.avatar)
      .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
      .setCustomContentView (collapseNotification)
      .setCustomBigContentView (ExpandedNotification)
      .setGroup (GROUP_KEY_WORK_EMAIL)
      .setGroupSummary (true)
      .setPriority (NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent (pendingIntent)
      .setBadgeIconType (NotificationCompat.BADGE_ICON_LARGE)
      .setNumber (1)
      .setAutoCancel (true);


    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    notificationManager.notify ((int) Math.random (), bigPictureNotification.build ());
  }


  @ReactMethod
  public void button() {
    RemoteViews ExpandedNotification = new RemoteViews (reactContext.getPackageName (), R.layout.caroussel_layout_notification);
    RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (), R.layout.collapse_notification);

    Intent button_intent = new Intent ("button_clicked");
    button_intent.putExtra ("id", 12);

    PendingIntent pendingIntent = PendingIntent.getBroadcast (reactContext, 123, button_intent, PendingIntent.FLAG_IMMUTABLE);
   // collapseNotification.setOnClickPendingIntent (R.id.button1, pendingIntent);

    NotificationCompat.Builder bigPictureNotification = new NotificationCompat.Builder (reactContext, "channelId")
      .setSmallIcon (R.drawable.avatar)
      .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
      .setCustomContentView (collapseNotification)
      .setCustomBigContentView (ExpandedNotification)
      .setGroup (GROUP_KEY_WORK_EMAIL)
      .setGroupSummary (true)
      .setPriority (NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent (pendingIntent)
      .setBadgeIconType (NotificationCompat.BADGE_ICON_LARGE)
      .setNumber (1)
      .setAutoCancel (true);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    notificationManager.notify ((int) Math.random (), bigPictureNotification.build ());
  }

  @ReactMethod
  public void CreateNotificationUrl(String title, String description, String avatarUrl,String url,String message){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel ("ChannelId", "big picture notification");
    }

    RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (), R.layout.collapse_notification);
    RemoteViews ExpandedNotification = new RemoteViews (reactContext.getPackageName (), R.layout.url_notification);

    Bitmap image = null;
    try {
      URL urlImage = new URL (avatarUrl);
      image = BitmapFactory.decodeStream (urlImage.openConnection ().getInputStream ());
    } catch (IOException e) {
      Log.d (TAG, String.valueOf (e));
    }

    collapseNotification.setTextViewText (R.id.title, title);
    collapseNotification.setTextViewText (R.id.description, description);

    ExpandedNotification.setTextViewText (R.id.title,title);
    ExpandedNotification.setTextViewText (R.id.description,description);
    ExpandedNotification.setTextViewText (R.id.msg,message);

    if (image != null) {
      collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
      collapseNotification.setImageViewBitmap (R.id.avatar, image);
      ExpandedNotification.setImageViewBitmap (R.id.avatar,image);
    }

    Intent intent = new Intent (Intent.ACTION_VIEW);
    intent.setData (Uri.parse (url));
    PendingIntent pendingIntent = PendingIntent.getActivity (reactContext,25,intent,0);

    NotificationCompat.Builder bigPictureNotification = new NotificationCompat.Builder (reactContext, "channelId")
      .setSmallIcon (R.drawable.avatar)
      .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
      .setCustomContentView (collapseNotification)
      .setCustomBigContentView (ExpandedNotification)
      .setGroup (GROUP_KEY_WORK_EMAIL)
      .setGroupSummary (true)
      .setPriority (NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent (pendingIntent)
      .setBadgeIconType (NotificationCompat.BADGE_ICON_LARGE)
      .setNumber (1)
      .setAutoCancel (true);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

    notificationManager.notify ((int) Math.random (), bigPictureNotification.build ());
  }
}
