package com.teco.vindi.touchdynamics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
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

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);

        mSoundMeter = new SoundMeter();
        mSoundMeter.registerListener(this);

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

        writeToCSV("button", "back");
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
            writeToCSV("touch", String.format(Locale.GERMAN, "%f", x), String.format(Locale.GERMAN, "%f", y));
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
                writeToCSV("rotated", "landscape");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                writeToCSV("rotated", "portrait");
                break;
            default:
                writeToCSV("rotated", "unknown");
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
            writeToCSV("accelerometer", String.format(Locale.GERMAN, "%f", sensorEvent.values[0]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[1]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[2]));
    }

    private void record_gyro(SensorEvent sensorEvent) {
        if (enabled("pref_rec_gyro"))
            writeToCSV("gyroscope", String.format(Locale.GERMAN, "%f", sensorEvent.values[0]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[1]),
                    String.format(Locale.GERMAN, "%f", sensorEvent.values[2]));
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
            writeToCSV("sound", String.format(Locale.GERMAN, "%f", soundLevel));
    }



    public void openCSV() {
        try {
            csvDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            csvDir += "/TouchDynamics/" + mTaskName + "_" + System.currentTimeMillis() + ".csv";

            csv = new CSVWriter(new FileWriter(csvDir), ';');
            String[] entries = {"time", "type", "value_x", "value_y", "value_z", "value_other"};
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

    public void writeToCSV( String eventType, String eventValue) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        long timestamp = System.currentTimeMillis();

        String[] entries = {""+ timestamp, eventType, null, null, null, eventValue};
        csv.writeNext(entries);
    }

    public void writeToCSV(String eventType, String eventValueX, String eventValueY) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        long timestamp = System.currentTimeMillis();

        String[] entries = {""+ timestamp, eventType, eventValueX, eventValueY, null, null};
        csv.writeNext(entries);
    }

    public void writeToCSV(String eventType, String eventValueX, String eventValueY, String eventValueZ) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        long timestamp = System.currentTimeMillis();

        String[] entries = {""+ timestamp, eventType, eventValueX, eventValueY, eventValueZ, null};
        csv.writeNext(entries);
    }
}
