package com.trianchatapps.Services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIdService extends FirebaseInstanceIdService {
    //udah dpreceated wkwkwk
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        Log.d("@@@@", "onTokenRefresh: " + instanceId);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("USER")
                    .child(firebaseUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }
}
