package com.trianchatapps.Services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.trianchatapps.Function;

public class InstanceIdService extends FirebaseInstanceIdService {
    //udah dpreceated wkwkwk
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        Log.d("@@@@", "onTokenRefresh: " + instanceId);
        try {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("USER")
                        .child(firebaseUser.getUid())
                        .child("instanceId")
                        .setValue(instanceId);
            }
        }catch (NullPointerException e){
            new Function.send_report(e.getMessage());
        }catch (Exception e){
            new Function.send_report(e.getMessage());
        }catch (Throwable e){
            new Function.send_report(e.getMessage());
        }
    }
}
