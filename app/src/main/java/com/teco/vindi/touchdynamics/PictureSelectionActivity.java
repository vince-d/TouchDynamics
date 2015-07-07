package com.teco.vindi.touchdynamics;

import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class PictureSelectionActivity extends MainActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTaskName("MenuNavigationTask");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());

        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                if (item.getTitle().equals("")) {
                    item.setTitle(" ");
                    item.setColor(Color.rgb(122, 255, 122));
                    gridAdapter.notifyDataSetChanged();
                } else {
                    item.setTitle("");
                    Color.argb(0, 0, 0, 0);
                    gridAdapter.notifyDataSetChanged();
                }

            }
        });



    }

    final R.drawable drawableResources = new R.drawable();
    final Class<R.drawable> c = R.drawable.class;
    final Field[] fields = c.getDeclaredFields();

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        // Get images from array
        //TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);

        // Load images dierctly from disk.
        // Change k < 1 to make list longer.
        for (int k = 0; k < 1; k++) {
            for (int i = 0, max = fields.length; i < max; i++) {
                final int resourceId;
                try {
                    resourceId = fields[i].getInt(drawableResources);

                } catch (Exception e) {
                    continue;
                }

                String name = getResources().getResourceEntryName(resourceId);

                if ((name.charAt(0)) == 'a' && (name.charAt(1)) == '_') {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    imageItems.add(new ImageItem(bitmap, "", Color.argb(0, 0, 0, 0)));
                }
            }
        }

        return imageItems;
    }




}
