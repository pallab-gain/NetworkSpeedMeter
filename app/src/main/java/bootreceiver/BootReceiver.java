package bootreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import networkmonitor.NetworkMonitorService;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent _intent = new Intent(context.getApplicationContext(), NetworkMonitorService.class);
            context.getApplicationContext().startService(_intent);
        }
    }
}