package com.teco.vindi.touchdynamics;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity implements SensorEventListener, SoundMeter.SoundLevelListener {

    private static final String _TAG = MainActivity.class.getName();

    private EditText mEditText;
    private SensorManager mSensorManager;
    private SoundMeter mSoundMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);

        mSoundMeter = new SoundMeter();
        mSoundMeter.registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundMeter.unregisterListener(this);
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Text watcher is added in onPostCreate to get around duplicate events on orientation change.
        mEditText.addTextChangedListener(new TextWatcher() {

            private int textLength = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = "";
                String newChar;

                if (s.length() <= textLength) {
                    newChar = "BACKSPACE";
                } else {
                    newChar = Character.toString(s.charAt(s.length() - 1));
                }

                if (s.length() != 0) {
                    newText = s.toString();
                }

                if (enabled("pref_rec_keys")) {
                    log("Typed=\"" + newChar + "\" Text=\"" + s.toString() + "\"");
                }

                textLength = s.length();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!enabled("pref_rec_back"))
            return;

        log("Back pressed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public boolean dispatchTouchEvent (MotionEvent motionEvent) {
        if (enabled("pref_rec_touch")) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            log("x=" + x + " y=" + y);
        }
       return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);

        if (!enabled("pref_rec_orientation"))
            return;

        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:
                log("Device rotated: Landscape");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                log("Device rotated: Portrait");
                break;
            default:
                log("Device rotated: Unknown");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                record_acc(sensorEvent);
                break;
            case Sensor.TYPE_GYROSCOPE:
                record_gyro(sensorEvent);
                break;
            default:
                break;
        }
    }

    private void record_acc(SensorEvent sensorEvent) {
        if (enabled("pref_rec_acc"))
            log("Accelerometer: " + sensorEvent.values[0] + " " + sensorEvent.values[1] + " " + sensorEvent.values[2]);
    }

    private void record_gyro(SensorEvent sensorEvent) {
        if (enabled("pref_rec_gyro"))
            log("Gyroscope: " + sensorEvent.values[0] + " " + sensorEvent.values[1] + " " + sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    /**
     * Logs the given event together with the time in milliseconds.
     * @param event
     */
    public void log(String event) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        long time = System.currentTimeMillis();
        Log.d(_TAG, df.format(time) + " - " + event);
    }

    public boolean enabled(String sensorName) {
        if (sensorName == null || sensorName.equals(""))
            return false;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(sensorName, false);
    }

    @Override
    public void onSoundLevelChanged(double soundLevel) {
        if (enabled("pref_rec_sound"))
            log("Sound level: " + soundLevel);
    }
}
