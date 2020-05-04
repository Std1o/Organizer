package com.stdio.organizer;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
//Осуществляем блокировку
        wl.acquire();
        Bundle extras= intent.getExtras();
        String name = null;
        String strFinishDate = null;
        boolean isShowed = false;
        Date currentDate = null, finishDate = null;
        if(extras != null) {
            name = extras.getString("name");
            strFinishDate = extras.getString("finishDate");
            isShowed = extras.getBoolean("showed");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String strCurrentDate = sdf.format(Calendar.getInstance().getTime());
        try {
            finishDate = sdf.parse(strFinishDate);
            currentDate = sdf.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("666", e.getMessage());
        }
        boolean inRange = currentDate.after(finishDate);
        boolean isCurrentDate = currentDate.compareTo(finishDate) == 0;
        if (inRange || isCurrentDate) {
            intent.putExtra("showed", false);
            sendNotification(context, " " + name);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi);
        }
        else {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi);
        }
//Разблокируем поток.
        wl.release();
    }

    public void SetAlarm(Context context, String finishDate, String name) {
        System.out.println(name);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);//Задаем параметр интента
        intent.putExtra("name", name);
        intent.putExtra("finishDate", finishDate);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//Устанавливаем интервал срабатывания в 5 секунд.
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*10, pi);
    }


    public void sendNotification(Context context, String messageBody) {
        int notificationCode = 378;

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultRingtone = null;
        defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("dayPlanner",
                    "Channel name",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel description");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context, "dayPlanner")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setSound(defaultRingtone)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentText(messageBody);
            notificationManager.notify(notificationCode, notificationCompat.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context, "dayPlanner")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setSound(defaultRingtone)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentText(messageBody);
            notificationManager.notify(notificationCode, notificationCompat.build());
        }

    }
}
