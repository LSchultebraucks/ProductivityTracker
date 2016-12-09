package net.bitdevelop.lasse.productivitytracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 0;
    public static final String ACTION = "net.bitdevelop.lasse.productivitytracker";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotificationIntentService.class);
        context.startService(i);
    }
}
