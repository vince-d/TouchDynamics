package com.teco.vindi.touchdynamics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SensorEventListener, SoundMeter.SoundLevelListener {

    private static final String _TAG = MainActivity.class.getName();


    private SensorManager mSensorManager;
    private SoundMeter mSoundMeter;

    private String csvDir;

    private CSVWriter csv;

    private String mTaskName = _TAG;

    public boolean mCognitiveLoad;
    public String mGroup;
    public boolean mPictureSetOne;

    DecimalFormat df;
    long timestamp = 0;

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);

        timestamp = System.currentTimeMillis();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);

        mSoundMeter = new SoundMeter();
        mSoundMeter.registerListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mCognitiveLoad = preferences.getBoolean("pref_cogload", false);
        mGroup = preferences.getString("pref_group", "A");

        if (mGroup.equals("A") && mCognitiveLoad == false) {
            mPictureSetOne = true;
        } else if (mGroup.equals("A") && mCognitiveLoad == true) {
            mPictureSetOne = false;
        } else if (mGroup.equals("B") && mCognitiveLoad == false) {
            mPictureSetOne = false;
        } else if (mGroup.equals("B") && mCognitiveLoad == true) {
            mPictureSetOne = true;
        } else if (mGroup.equals("C") && mCognitiveLoad == false) {
            mPictureSetOne = false;
        } else if (mGroup.equals("C") && mCognitiveLoad == true) {
            mPictureSetOne = true;
        } else if (mGroup.equals("D") && mCognitiveLoad == false) {
            mPictureSetOne = true;
        } else if (mGroup.equals("D") && mCognitiveLoad == true) {
            mPictureSetOne = false;
        }

        openCSV();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundMeter.unregisterListener(this);
        mSensorManager.unregisterListener(this);
        closeCSV();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!enabled("pref_rec_back"))
            return;

        writeToCSV("button", null, null, null, null, null, null, null, null, null, "back", null);
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
            writeToCSV("touch", null, null, null, null, null, null,
                        String.format(Locale.GERMAN, "%f", x), String.format(Locale.GERMAN, "%f", y),
                        null, null, null);
        }
       return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);

        // Rotation is not used.

        if (!enabled("pref_rec_orientation"))
            return;

        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:
                //writeToCSV("rotated", "landscape");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //writeToCSV("rotated", "portrait");
                break;
            default:
                //writeToCSV("rotated", "unknown");
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
            writeToCSV("accelerometer",
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[0]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[1]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[2]),
                    null, null, null, null, null, null, null, null);
    }

    private void record_gyro(SensorEvent sensorEvent) {
        if (enabled("pref_rec_gyro"))
            writeToCSV("gyroscope", null, null, null,
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[0]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[1]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[2]),
                    null, null, null, null, null);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
            writeToCSV("sound", null, null, null, null, null, null, null, null, null, null, String.format(Locale.GERMAN, "%f", soundLevel));
    }



    public void openCSV() {
        try {
            String CL = "NoCL";
            if (mCognitiveLoad) {
                CL = "CL";
            }

            csvDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            csvDir += "/TouchDynamics/" + mGroup + "_" + CL + "_" + mTaskName + "_" + System.currentTimeMillis() + ".csv";

            csv = new CSVWriter(new FileWriter(csvDir), ';');
            String[] entries = {"time", "type", "acc_x", "acc_y", "acc_z", "gyr_x", "gyr_y", "gyr_z", "touch_x", "touch_y", "keyboard", "button", "sound"};
            csv.writeNext(entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCSV() {
        try {
            csv.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(csvDir))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToCSV(String type, String acc_x, String acc_y, String acc_z, String gyr_x, String gyr_y, String gyr_z, String touch_x, String touch_y, String keyboard, String button, String sound) {

        long timestamp = System.currentTimeMillis();

        String[] entries = {""+ timestamp, type, acc_x, acc_y, acc_z, gyr_x, gyr_y, gyr_z, touch_x, touch_y, keyboard, button, sound};
        csv.writeNext(entries);
    }
}
