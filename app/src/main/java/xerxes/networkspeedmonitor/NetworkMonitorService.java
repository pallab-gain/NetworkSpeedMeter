package xerxes.networkspeedmonitor;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
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
    private Handler mHandler;
    private static long prvRcv,prvTrs;
    public void onCreate() {
        super.onCreate();
        prvRcv=-1;
        prvTrs=-1;
        mHandler = new Handler();
        textView = new TextView(this);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                doit();
            }
        });

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final LayoutParams myParams = new LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_PHONE,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;

        windowManager.addView(textView,myParams);
        try{
        	//for moving the picture on touch and slide
        	textView.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                	if(System.currentTimeMillis()-touchStartTime>ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
                		windowManager.removeView(textView);
                		stopSelf();
                		return false;
                		
                	}
                	switch(event.getAction()){
                    
                    
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
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private synchronized void setSpeed(String speed){
        if(textView!=null) {
            textView.setText(speed);
        }
    }
    private void doit(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long bytesTransmitted = TrafficStats.getTotalTxBytes();
                long bytesReceived    = TrafficStats.getTotalRxBytes();
                setSpeed(getString(bytesTransmitted-prvTrs,bytesReceived-prvRcv));
                prvRcv=bytesReceived;
                prvTrs=bytesTransmitted;
                if(mHandler!=null){
                    mHandler.postDelayed(this,1000L);
                }
            }
        },1000L);
    }
    private String getString(long trns, long rcv){
        StringBuilder sb = new StringBuilder();
        sb.append("Up: ").append(trns/1024).append(" KB/s");
        sb.append("     Dn: ").append(rcv/1024).append(" KB/s");

        return sb.toString();
    }

    @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
    
}