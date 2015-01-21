package networkmonitor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.TextView;

import MyUtils.Constants;
import screenlistnerpkg.ScreenListner;
import xerxes.networkspeedmonitor.MainActivity;
import xerxes.networkspeedmonitor.R;

import static MyUtils.Constants.NOTIFICATION_ID.FOREGROUND_SERVICE;

public class NetMonitorService extends Service {
    public NetMonitorService() {
    }

    private WindowManager windowManager;
    private TextView textView;
    private Handler mHandler;
    private long total_rcv;
    private long total_send;
    private long prev_upload_speed;
    private long prev_download_speed;

    //screen parameters
    final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    @Override
    public void onCreate() {
        super.onCreate();

        textView = new TextView(getApplicationContext());
        textView.setMinWidth(400);
        textView.setMaxWidth(400);
        textView.setTypeface(null, Typeface.BOLD);

        mHandler = new Handler();
        total_rcv = TrafficStats.getTotalRxBytes();
        total_send = TrafficStats.getTotalTxBytes();
        prev_download_speed = -1;
        prev_upload_speed = -1;

        windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        myParams.gravity = Gravity.TOP | Gravity.CENTER;
        windowManager.addView(textView, myParams);
        try {
            textView.setOnTouchListener(onTouchListener);
        } catch (Exception e) {
            e.printStackTrace();
        }


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenListner();
        registerReceiver(mReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION) || intent.getAction().equals(Constants.ACTION.BOOT_RECEIVE)) {

            if(mHandler!=null){
                mHandler.removeCallbacks(runnable);
                mHandler.post(runnable);
            }

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Network Speed Monitor")
                    .setTicker("Network Speed Monitor")
                    .setContentText("Network Speed Monitor")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent);

            Notification cur_notification = builder.build();
            startForeground(FOREGROUND_SERVICE, cur_notification);
        }else if(intent.getAction().equals(Constants.ACTION.START_REPEAT)){
            if(mHandler!=null){
                mHandler.post(runnable);
            }
        }else if(intent.getAction().equals(Constants.ACTION.STOP_REAP)){
            if(mHandler!=null){
                mHandler.removeCallbacks(runnable);
            }
        }
        return START_STICKY;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //do the job
            final long download_speed =  (TrafficStats.getTotalRxBytes()-total_rcv)/1024;
            final long upload_speed = (TrafficStats.getTotalTxBytes()-total_send)/1024;
            if(textView!=null && (prev_download_speed!= download_speed || prev_upload_speed != upload_speed) ){
                prev_download_speed = download_speed;
                prev_upload_speed = upload_speed;
                textView.setText( String.format("D :%4s KB/S%4sU :%4s KB/S", download_speed, "", upload_speed) );
            }
            total_rcv = TrafficStats.getTotalRxBytes();
            total_send = TrafficStats.getTotalTxBytes();
            if(mHandler!=null){
                mHandler.postDelayed(this,1000L*1); /*every 1 second*/
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textView != null) {
            if (windowManager != null) {
                windowManager.removeView(textView);
                windowManager = null;
            }
            textView = null;
        }
        if(mHandler!=null){
            mHandler.removeCallbacks(runnable);
            mHandler=null;
        }
    }

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        private long touchStartTime = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                windowManager.removeView(textView);
                stopSelf();
                return false;

            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStartTime = System.currentTimeMillis();
                    initialX = myParams.x;
                    initialY = myParams.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                    myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(v, myParams);
                    break;
            }
            return false;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
