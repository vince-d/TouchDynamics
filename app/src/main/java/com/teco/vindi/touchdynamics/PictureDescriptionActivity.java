package com.teco.vindi.touchdynamics;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;


public class PictureDescriptionActivity extends MainActivity {

    private EditText mEditText;
    private ImageView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTaskName("TextWritingTask");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_description);

        //sample
        mEditText = (EditText) findViewById(R.id.editText);
        mPicture = (ImageView) findViewById(R.id.imageView);
        if (mPictureSetOne) {
            mPicture.setImageResource(R.drawable.cat_describe);
        } else {
            mPicture.setImageResource(R.drawable.dog_describe);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mEditText != null) {
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
                        writeToCSV("keyboard", "typed=\"" + newChar + "\" text=\"" + s.toString() + "\"");
                    }

                    textLength = s.length();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_description, menu);
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
}
