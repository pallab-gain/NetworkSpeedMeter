package xerxes.networkspeedmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import MyUtils.Constants;
import MyUtils.Preference;
import networkmonitor.NetMonitorService;


public class MainActivity extends ActionBarActivity {

    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar)findViewById(R.id.seek_bar_font_size);
        seekBar.setOnSeekBarChangeListener(seekBarListner);

        Intent startIntent = new Intent(MainActivity.this, NetMonitorService.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        seekBar.setProgress(Preference.getInstance(getApplicationContext()).getFont_size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //seek bar listner
    private final SeekBar.OnSeekBarChangeListener seekBarListner = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Preference.getInstance(getApplicationContext()).setFont_size(progress);
            Intent startIntent = new Intent(getApplicationContext(), NetMonitorService.class);
            startIntent.setAction(Constants.ACTION.UPDATE_FONT_SIZE);
            startService(startIntent);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
