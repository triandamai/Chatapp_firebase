package com.trianchatapps;

import android.graphics.*;
import android.os.AsyncTask;

import android.os.Build;
import android.support.annotation.Keep;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.Model.ReportModel;
import com.trianchatapps.Model.StatusAktifModel;

import com.trianchatapps.Model.UserModel;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Function implements Serializable {


    public static class IsOnline extends AsyncTask<String, Void, String> {
        public DatabaseReference databaseReference;
        public FirebaseUser firebaseUser;
        public long timestamp = new Date().getTime();
        public UserModel user;

        @Keep
        @Override
        protected String doInBackground(String... strings) {


            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            StatusAktifModel statusAktif = new StatusAktifModel(1, timestamp);
            if (firebaseUser != null) {
                databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                        .child(firebaseUser.getUid())
                        .setValue(statusAktif);
            } else {

            }

            return "anda off";
        }

    }

    public static class IsOffline extends AsyncTask<String, Void, String> {
        public DatabaseReference databaseReference;
        public FirebaseUser firebaseUser;
        public long timestamp = new Date().getTime();

        @Keep
        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();


            StatusAktifModel statusAktif = new StatusAktifModel(2, timestamp);
            if (firebaseUser != null) {
                databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                        .child(firebaseUser.getUid())
                        .setValue(statusAktif);
            } else {

            }
            return "anda off";
        }

    }


    public static class IsTyping extends AsyncTask<String, Void, String> {
        public DatabaseReference databaseReference;
        public FirebaseUser firebaseUser;
        public long timestamp = new Date().getTime();


        @Keep
        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            StatusAktifModel statusAktif = new StatusAktifModel(3, timestamp);
            databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                    .child(firebaseUser.getUid())
                    .setValue(statusAktif);

            return "anda off";
        }

    }

    public static String getDatePretty(long timestamp, boolean showTimeOfDay) {
        DateTime yesterdayDT = new DateTime(
                DateTime
                        .now()
                        .getMillis() - 1000 * 60 * 60 * 24);

        yesterdayDT = yesterdayDT
                .withTime(0, 0, 0, 0);

        Interval today = new Interval(
                DateTime
                        .now()
                        .withTimeAtStartOfDay(), Days.ONE);

        Interval yesterday = new Interval(yesterdayDT, Days.ONE);

        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();

        if (today.contains(timestamp)) {

            if (showTimeOfDay) {
                return timeFormatter.print(timestamp);
            } else {
                return "Hari ini" + " " + timeFormatter.print(timestamp);
            }

        } else if (yesterday.contains(timestamp)) {

            return "Kemarin" + " " + timeFormatter.print(timestamp);

        } else {

            return dateFormatter.print(timestamp);

        }
    }

    public static long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public Bitmap getRoundbmp(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


}
