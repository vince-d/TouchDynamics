package com.teco.vindi.touchdynamics;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * The config screen of the main map view.
 */
public class ConfigActivity extends AppCompatActivity {

    // The main view of this activity.
    private View mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        setTitle(R.string.activity_config_title);
    }

    /**
     * The preference fragment. Load preferences from XML.
     */
    public static class ConfigFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_record);

        }
    }

}
