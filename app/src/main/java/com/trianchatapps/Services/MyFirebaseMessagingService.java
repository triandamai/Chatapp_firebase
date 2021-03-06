package com.trianchatapps.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.support.v4.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.trianchatapps.Function;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FIREBASEMEssagingService";
    public static final String REPLY_ACTION = "reply";
    public static final String KEY_NOTIFICATION_ID = "id_notif";
    public static final int NOTIFICATION_ID = 1;
    public static final String ISI_PESAN = "id";
    public static final String REPLY_KEY = "key";
    public static final String NOTIFICATION_CHANNEL_ID = "ChatsApp";
    public String instanceId = null;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        instanceId = s;

        Log.d("@@@@", "onTokenRefresh: " + instanceId);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            if (firebaseUser != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("USER")
                        .child(firebaseUser.getUid())
                        .child("instanceId")
                        .setValue(instanceId);
            }
        } catch (NullPointerException e) {
            Crashlytics.logException(e);
        } catch (Exception e) {
            Crashlytics.logException(e);
        } catch (Throwable e) {
            Crashlytics.logException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String notificationTitle = null,
                notificationBody = null,
                mesfrom = null,
                userIMg = null;

        // Check if message contains a notification payload.
        if (remoteMessage.getData().size() > 0) {
            // Device to device
            try {


                notificationTitle = remoteMessage.getData().get("title");
                notificationBody = remoteMessage.getData().get("body");
                mesfrom = remoteMessage.getData().get("user_id");
                userIMg = remoteMessage.getData().get("icon");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    //TODO :: Notifikasi android 0 keatas
                    sendNotificationP(notificationTitle, notificationBody, mesfrom, userIMg);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendNotificationO(notificationTitle, notificationBody, mesfrom, userIMg);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //TODO :: Notifikasi android N keatas
                    sendNotificationN(notificationTitle, notificationBody, mesfrom, userIMg);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //TODO :: Notifikasi android M keatas
                    sendNotificationM(notificationTitle, notificationBody, mesfrom, userIMg);
                } else {
                    //TODO :: Notifikasi android selain M,N,P
                    sendNotificationBelow(notificationTitle, notificationBody, mesfrom, userIMg);
                }
            } catch (NullPointerException e) {

            } catch (Exception e) {
                Crashlytics.logException(e);
            } catch (Throwable e) {
                Crashlytics.logException(e);
            }

        } else if (remoteMessage.getNotification() != null) {
            //FCM
        } else {

        }

    }

    public void sendNotificationBelow(String notificationTitle, String notificationBody, String mesfrom, String userIMg) {

        Intent intent = new Intent(this, ThreadChat.class);
        intent.putExtra("uid", mesfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(userIMg).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(new Function().getRoundbmp(bmp))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }

    public void sendNotificationM(String notificationTitle, String notificationBody, String mesfrom, String userIMg) {
        Intent intent = new Intent(this, ThreadChat.class);
        intent.putExtra("uid", mesfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(userIMg).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        long time = new Date().getTime();


        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(new Function().getRoundbmp(bmp))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }

    public void sendNotificationO(String notificationTitle, String notificationBody, String mesfrom, String userIMg) {
        Intent intent = new Intent(this, ThreadChat.class);
        intent.putExtra("uid", mesfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(userIMg).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteInput remoteInput = new RemoteInput.Builder(REPLY_KEY)
                .setLabel("Enter untuk membalas")
                .build();
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send,
                "Balas Sekarang", pendding(mesfrom, notificationBody))
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(notificationBody))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(new Function().getRoundbmp(bmp))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .addAction(action)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }


    //TODO :: android Nougat
    public void sendNotificationN(String notificationTitle, String notificationBody, String mesfrom, String userIMg) {
        Intent intent = new Intent(this, ThreadChat.class);
        intent.putExtra("uid", mesfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(userIMg).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteInput remoteInput = new RemoteInput.Builder(REPLY_KEY)
                .setLabel("Enter untuk membalas")
                .build();
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send,
                "Balas Sekarang", pendding(mesfrom, notificationBody))
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(notificationBody))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(new Function().getRoundbmp(bmp))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .addAction(action)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void sendNotificationP(String notificationTitle, String notificationBody, String from, String img) {
        Intent intent = new Intent(this, ThreadChat.class);
        intent.putExtra("uid", from);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(img).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            Crashlytics.logException(e);
        } catch (IOException e) {
            Crashlytics.logException(e);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_ID = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
            CHANNEL_ID.setDescription(NOTIFICATION_CHANNEL_ID);
            CHANNEL_ID.enableLights(true);
            CHANNEL_ID.enableVibration(true);
            //CHANNEL_ID.setVibrationPattern(new long[]{100, 200 ,400 ,500,400,300,200,400,100});
            notificationManager.createNotificationChannel(CHANNEL_ID);
        }

        Person user = new Person.Builder().setName(notificationTitle)
                .setIcon(null)
                .build();
        long time = new Date().getTime();
        Notification.MessagingStyle.Message message = new Notification.MessagingStyle.Message(notificationBody, time, notificationTitle);

        RemoteInput remoteInput = new RemoteInput.Builder(REPLY_KEY)
                .setLabel("Enter untuk membalas")
                .build();
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(android.R.drawable.ic_delete,
                "Balas Sekarang", pendding(from, notificationBody))
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();
        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(notificationTitle);
        style.addMessage(notificationBody, time, notificationTitle);
        style.setGroupConversation(true);

        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(style)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(new Function().getRoundbmp(bmp))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .addAction(action)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);

    }

    public PendingIntent pendding(String from, String body) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(this, NotifikasiService.class);
            intent.setAction(REPLY_ACTION);
            intent.putExtra(KEY_NOTIFICATION_ID, from);
            intent.putExtra(ISI_PESAN, body);
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intent = new Intent(this, NotifikasiService.class);
            intent.setAction(REPLY_ACTION);
            intent.putExtra(ISI_PESAN, from);
            intent.putExtra(KEY_NOTIFICATION_ID, body);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(this, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

    }


}
