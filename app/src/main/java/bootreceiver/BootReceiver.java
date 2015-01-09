package bootreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import repeater.AlarmManagerHandler;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if(AlarmManagerHandler.getInstance(context)==null) {
                AlarmManagerHandler.getInstance(context);
            }
        }
    }
}