package screenlistnerpkg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import MyUtils.Constants;
import networkmonitor.NetMonitorService;

public class ScreenListner extends BroadcastReceiver {
    public ScreenListner() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent startIntent = new Intent(context, NetMonitorService.class);
            startIntent.setAction(Constants.ACTION.STOP_REAP);
            context.startService(startIntent);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Intent startIntent = new Intent(context, NetMonitorService.class);
            startIntent.setAction(Constants.ACTION.START_REPEAT);
            context.startService(startIntent);
        } else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent startIntent = new Intent(context, NetMonitorService.class);
            startIntent.setAction(Constants.ACTION.BOOT_RECEIVE);
            context.startService(startIntent);
        }
    }
}
