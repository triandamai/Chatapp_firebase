package com.trianchatapps.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    public boolean CekKoneksi(Context context) {
        boolean flag = false;
        try{
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                flag = true;
            }

        } catch (Exception e){
            new Bantuan(context).alertDialogPeringatan(e.getMessage());
        }
        return flag;
    }
}
