package repeater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by xerxes on 1/10/15.
 * "I have not failed, I have just found 10000 ways that won't work."
 */
public class AlarmManagerHandler {
    private static AlarmManagerHandler ourInstance = null;
    private final String tag = AlarmManagerHandler.class.getSimpleName();

    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    private Context mContext;
    private final long interval = 1L * 1000; /*fire alarm evey one seconds*/

    public synchronized static AlarmManagerHandler getInstance(final Context context) {
        if (ourInstance == null) {
            ourInstance = new AlarmManagerHandler(context);
        }
        return ourInstance;
    }

    private AlarmManagerHandler(Context context) {

        //1:
        if (alarmMgr != null && alarmIntent != null) {
            alarmMgr.cancel(alarmIntent);
        }
        //:2
        if (alarmIntent != null) {
            alarmIntent = null;
        }

        mContext = context;
        alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(mContext, AlarmManagerReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), 0, intent, 0);
        Calendar cal = Calendar.getInstance();
        alarmMgr.setInexactRepeating(AlarmManager.RTC,
                cal.getTimeInMillis(), interval, alarmIntent);

    }
}
