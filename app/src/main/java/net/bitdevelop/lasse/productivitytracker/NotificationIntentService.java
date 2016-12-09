package net.bitdevelop.lasse.productivitytracker;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationIntentService extends IntentService {

    public NotificationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("NotificationServiceApp", "Service running");

        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean shouldNotify = sharedPref.getBoolean("key_enable_notifications", true);

        if (shouldNotify) {
            String startHourString = sharedPref.getString("key_start_notification", "9");
            int startHour = Integer.parseInt(startHourString);
            String endHourString = sharedPref.getString("key_end_notification", "17");
            int endHour = Integer.parseInt(endHourString);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (startHour <= calendar.get(Calendar.HOUR_OF_DAY) && endHour > calendar.get(Calendar.HOUR_OF_DAY)) {
                if (!isForeground(getPackageName())) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.productivity_tracker_icon_white)
                            .setTicker(getApplicationContext().getText(R.string.add_new_entry))
                            .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                            .setContentTitle(getApplicationContext().getString(R.string.status_acquisition))
                            .setContentText(getApplicationContext().getString(R.string.how_is_your_motivation));

                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    Intent newEntryIntent = new Intent(getApplicationContext(), NewEntryActivity.class);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addNextIntent(mainIntent);
                    stackBuilder.addNextIntent(newEntryIntent);

                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
                    mBuilder.setContentIntent(pendingIntent);

                    mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);

                    mBuilder.setAutoCancel(true);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(0, mBuilder.build());
                }
            }
        }

    }

    private boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(myPackage);
    }
}
