package com.teco.vindi.touchdynamics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class PictureSelectionActivity extends MainActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private List<Integer> selected;
    private int[] correct;
    private int[] correctSet1 = {12,13,21,44,56,60,66,81,104,107};
    private int[] correctSet2 = { 2, 5, 8,24,32,33,49,54, 67,107};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTaskName("MenuNavigationTask");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);

        selected = new LinkedList<>();
        if (mPictureSetOne) {
            correct = correctSet1;
        } else {
            correct = correctSet2;
        }

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
                    selected.add(position);
                } else {
                    item.setTitle("");
                    item.setColor(Color.argb(0, 0, 0, 0));
                    gridAdapter.notifyDataSetChanged();
                    if (selected.contains(position)) {
                        selected.remove(selected.indexOf(position));
                    }
                }

                boolean allCorrect = true;

                if (selected.size() == correct.length) {
                    for (int i : correct) {
                        if (selected.contains(i)) {
                            // Correct picture selected.
                        } else {
                            // At least one was wrong.
                            allCorrect = false;
                        }
                    }
                } else {
                    allCorrect = false;
                }

                // All correct pictures were selected. End task.
                if (allCorrect) {
                    onBackPressed();
                }

                Log.d("bb", "" + position);
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
                char pictureSetStartChar;

                if (mPictureSetOne == true) {
                    pictureSetStartChar = 'a';
                } else {
                    pictureSetStartChar = 'b';
                }
                if ((name.charAt(0)) == pictureSetStartChar && (name.charAt(1)) == '_') {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    imageItems.add(new ImageItem(bitmap, "", Color.argb(0, 0, 0, 0)));
                }
            }
        }

        return imageItems;
    }




}
