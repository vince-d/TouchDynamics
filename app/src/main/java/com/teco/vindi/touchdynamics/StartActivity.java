package com.teco.vindi.touchdynamics;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Config selected.
        if (id == R.id.action_settings) {
            Intent startSettingsIntent = new Intent(StartActivity.this, ConfigActivity.class);
            startActivity(startSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void taskOneButtonPressed(View view) {
        Intent startRecordActivity = new Intent(StartActivity.this, ScrollViewActivity.class);
        startActivity(startRecordActivity);
    }

    public void taskTwoButtonPressed(View view) {
        Intent startRecordActivity = new Intent(StartActivity.this, ScrollViewActivity.class);
        startActivity(startRecordActivity);
    }

}
