package com.trianchatapps;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.Model.StatusAktif;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

public class Function {
    private DatabaseReference databaseReference;

    public static class IsOnline extends AsyncTask<String, Void, String> {
        private DatabaseReference databaseReference;
        private FirebaseUser firebaseUser;
        long timestamp = new Date().getTime();


        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);

            StatusAktif statusAktif = new StatusAktif(1, timestamp);
            databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                    .child(firebaseUser.getUid())
                    .setValue(statusAktif);

            return "anda off";
        }

    }
    public static class IsOffline extends AsyncTask<String, Void, String> {
        private DatabaseReference databaseReference;
        private FirebaseUser firebaseUser;
        long timestamp = new Date().getTime();


        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);

            StatusAktif statusAktif = new StatusAktif(2, timestamp);
            databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                    .child(firebaseUser.getUid())
                    .setValue(statusAktif);

            return "anda off";
        }

    }


    public static class IsTyping extends AsyncTask<String, Void, String> {
        private DatabaseReference databaseReference;
        private FirebaseUser firebaseUser;
        long timestamp = new Date().getTime();


        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);

            StatusAktif statusAktif = new StatusAktif(3, timestamp);
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
    



}
