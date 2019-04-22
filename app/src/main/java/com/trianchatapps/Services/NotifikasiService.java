package com.trianchatapps.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
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
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Main.MainActivity;
import com.trianchatapps.Model.Message;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;



public class NotifikasiService extends BroadcastReceiver {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null){
        if (MyFirebaseMessagingService.REPLY_ACTION.equals(intent.getAction())) {
            CharSequence pesannya = getReplyMessage(intent);
            final String from = intent.getStringExtra(MyFirebaseMessagingService.KEY_NOTIFICATION_ID);


            long timestam = new Date().getTime();
            long datTimestamp = Function.getDayTimestamp(timestam);
            Message message =
                    new Message(timestam, -timestam, datTimestamp, pesannya.toString(), user.getUid(), from, 1);

            if (user != null) {
                final String id_saya = databaseReference.getKey();
                databaseReference
                        .child(GlobalVariabel.CHILD_CHAT)
                        .child(from)
                        .child(user.getUid())
                        .child(id_saya)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference
                                .child(GlobalVariabel.CHILD_CHAT)
                                .child(from)
                                .child(user.getUid())
                                .child(id_saya)
                                .child("tick")
                                .setValue(2);
                    }
                });
                if (!user.getUid().equals(from)) {
                    final String id_ke = databaseReference.push().getKey();
                    databaseReference
                            .child(GlobalVariabel.CHILD_CHAT)
                            .child(user.getUid())
                            .child(from)
                            .child(id_ke)
                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseReference.child(GlobalVariabel.CHILD_CHAT)
                                    .child(user.getUid())
                                    .child(from)
                                    .child(id_ke)
                                    .child("tick")
                                    .setValue(2);
                        }
                    });
                }
                databaseReference.child(GlobalVariabel.CHILD_NOTIF)
                        .push()
                        .setValue(message);
                Notification repliedNotification =
                        new Notification.Builder(context)
                                .setSmallIcon(
                                        android.R.drawable.ic_dialog_info)
                                .setContentText("Reply received")
                                .build();

                @SuppressLint("ServiceCast") NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                notificationManager.notify(1,
                        repliedNotification);

            }
        }else {

        }


        }





    }

    private CharSequence getReplyMessage(Intent intent){
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(MyFirebaseMessagingService.REPLY_KEY);
        }
        return null;
    }
}
