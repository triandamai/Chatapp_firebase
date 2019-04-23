package com.trianchatapps.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.MessageModel;

import java.util.Date;



public class NotifikasiService extends BroadcastReceiver {
    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        try {



        if (remoteInput != null){
        if (MyFirebaseMessagingService.REPLY_ACTION.equals(intent.getAction())) {
            CharSequence pesannya = getReplyMessage(intent);
            final String from = intent.getStringExtra(MyFirebaseMessagingService.KEY_NOTIFICATION_ID);


            long timestam = new Date().getTime();
            long datTimestamp = Function.getDayTimestamp(timestam);
            MessageModel message =
                    new MessageModel(timestam, -timestam, datTimestamp, pesannya.toString(), user.getUid(), from, 1);

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

            }
        }else {

        }
        }
        }catch (NullPointerException e){
            Crashlytics.logException(e);
        }catch (Exception e){
            Crashlytics.logException(e);
        }catch (Throwable e){
            Crashlytics.logException(e);
        }


        }







    public CharSequence getReplyMessage(Intent intent){
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(MyFirebaseMessagingService.REPLY_KEY);
        }
        return null;
    }
}
