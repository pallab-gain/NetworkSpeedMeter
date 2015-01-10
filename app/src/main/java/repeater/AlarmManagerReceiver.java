package repeater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import networkmonitor.NetworkMonitorService;

public class AlarmManagerReceiver extends BroadcastReceiver {
    private final String tag = AlarmManagerReceiver.class.getSimpleName();
    public AlarmManagerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        //:1
        if(NetworkMonitorService.getInstance()==null){
            NetworkMonitorService.getInstance(context);
        }

        NetworkMonitorService.getInstance().doit();
    }
}
