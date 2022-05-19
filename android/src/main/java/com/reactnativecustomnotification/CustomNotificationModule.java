package com.reactnativecustomnotification;

import static com.facebook.react.views.textinput.ReactTextInputManager.TAG;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
    private void createNotificationChannel(Context context,String ChannelId, String name){
      NotificationManager notificationManager = (NotificationManager) context.getSystemService (Context.NOTIFICATION_SERVICE);

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
    @ReactMethod
    public void multiply(int a, int b, Promise promise) {
        promise.resolve(a + b);
    }

    @ReactMethod
    public int test(int x, int y){
      return (x+y)*2;
    }

    @ReactMethod
    public void CreateInformativeNotification(String Title,String Description, String ImageUrl){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createNotificationChannel (reactContext,"channelId","informative notification");
      }

      Bitmap image = null;
      try {
       URL url = new URL (ImageUrl);
        image = BitmapFactory.decodeStream (url.openConnection ().getInputStream ());
      } catch (IOException e) {
        Log.d (TAG, String.valueOf (e));
      }


      RemoteViews collapseNotification = new RemoteViews (reactContext.getPackageName (),R.layout.collapse_notification);
      collapseNotification.setTextViewText (R.id.title,Title);
      collapseNotification.setTextViewText (R.id.description,Description);

      if (image != null ){
        collapseNotification.setViewVisibility (R.id.avatar, View.VISIBLE);
        collapseNotification.setImageViewBitmap (R.id.avatar,image);
      }

      Notification informativeNotification = new NotificationCompat.Builder (reactContext,"channelId")
        .setSmallIcon (R.drawable.avatar)
        .setStyle (new NotificationCompat.DecoratedCustomViewStyle ())
        .setCustomContentView (collapseNotification)
        .setAutoCancel (true)
        .build ();

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from (reactContext);

      notificationManager.notify ((int) Math.random (),informativeNotification);
    }

    //public static native int nativeMultiply(int a, int b);
}
