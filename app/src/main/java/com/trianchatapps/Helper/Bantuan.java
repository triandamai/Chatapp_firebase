package com.trianchatapps.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

public class Bantuan {
    Context context;

    public Bantuan(Context context){
        this.context = context;
    }
    public void toastShort( String pesan) {
        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(String pesan) {
        Toast.makeText(context, pesan, Toast.LENGTH_LONG).show();
    }

    public void alertDialogDebugging( String pesan) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Info Debugging")
                .setMessage(pesan)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void alertDialogPeringatan( String pesan) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Peringatan")
                .setMessage(pesan)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void alertDialogInformasi( String pesan) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Informasi")
                .setMessage(pesan)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


}
