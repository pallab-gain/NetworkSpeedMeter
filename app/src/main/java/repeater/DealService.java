package repeater;

import android.app.IntentService;
import android.content.Intent;

import networkmonitor.NetworkMonitorService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DealService extends IntentService {
    private final String tag = DealService.class.getSimpleName();
    public DealService() {
        super("DealService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(NetworkMonitorService.getInstance()!=null) {
            NetworkMonitorService.getInstance().doit();
        }else{
        }
    }
}
