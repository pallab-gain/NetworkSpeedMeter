package networkmonitor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;


public class NetworkMonitorService extends Service {
    private WindowManager windowManager;
    private TextView textView;
    private Context mContext;
    private static long prvRcv, prvTrs;
    private static NetworkMonitorService networkMonitorService;
    private Handler mHandler;
    public static NetworkMonitorService getInstance(Context context) {
        if (networkMonitorService == null) {
            networkMonitorService = new NetworkMonitorService(context);
        }
        return networkMonitorService;
    }
    public static NetworkMonitorService getInstance() {
        return networkMonitorService;
    }

    //screen parameters
    final LayoutParams myParams = new LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_PHONE,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    public NetworkMonitorService(Context context) {
        prvRcv = prvTrs = -1;
        mContext = context;
        mHandler = new Handler();
        if (textView == null) {
            textView = new TextView(context);
            textView.setMinWidth(400);
            textView.setMaxWidth(400);
            textView.setTypeface(null, Typeface.BOLD);
        }
        windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);

        myParams.gravity = Gravity.TOP | Gravity.CENTER;
        myParams.x = 0;
        myParams.y = 100;
        windowManager.addView(textView, myParams);

        try {
            textView.setOnTouchListener(onTouchListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void setSpeed(final String speed) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if (textView != null) {
                    textView.setText(speed);
                }
            }
        });
    }

    public void doit() {

        final long bytesTransmitted = TrafficStats.getTotalTxBytes();
        final long bytesReceived = TrafficStats.getTotalRxBytes();
        if (prvRcv >= 0 && prvTrs >= 0) {
            setSpeed(getString(bytesTransmitted - prvTrs, bytesReceived - prvRcv));
        }
        prvRcv = bytesReceived;
        prvTrs = bytesTransmitted;

    }

    private synchronized String getString(final long trns, final long rcv) {
        return String.format("D :%4s KB/S%4sU :%4s KB/S", rcv / 1024, "", trns / 1024);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

}