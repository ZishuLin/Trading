package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.core.app.ActivityCompat.checkSelfPermission;
import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertEmployee{

    static String channelID = "Notification";
    private static final Integer REQUEST_CODE = 111;
    int notificationID = 0;
    private Context context;
    private Activity activity;


    public AlertEmployee(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void sendNotification(String jobType) {
        NotificationCompat.Builder builder = null;

        builder = new NotificationCompat.Builder(context, channelID);

        builder.setSmallIcon(R.drawable.logo_small);
        builder.setContentTitle("New Job Available");
        builder.setContentText("New Job: " + jobType + " is available in your local area!");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent nIntent = new Intent(activity, Dashboard.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(pIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(channelID, "Custom", NotificationManager.IMPORTANCE_HIGH));
            if (checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
            } else {
                manager.notify(notificationID, builder.build());
            }
        }

    }

}
