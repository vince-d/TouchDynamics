package com.teco.vindi.touchdynamics;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class ScrollViewActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // this is data fro recycler view
        ScrollViewData itemsData[] = {
                new ScrollViewData("Berlin",R.drawable.ic_map),
                new ScrollViewData("Hamburg",R.drawable.ic_map),
                new ScrollViewData("Muenchen",R.drawable.ic_map),
                new ScrollViewData("Koeln",R.drawable.ic_map),
                new ScrollViewData("Frankfurt am Main",R.drawable.ic_map),
                new ScrollViewData("Stuttgart",R.drawable.ic_map),
                new ScrollViewData("Duesseldorf",R.drawable.ic_map),
                new ScrollViewData("Dortmund",R.drawable.ic_map),
                new ScrollViewData("Essen",R.drawable.ic_map),
                new ScrollViewData("Bremen",R.drawable.ic_map),
                new ScrollViewData("Leipzig",R.drawable.ic_map),
                new ScrollViewData("Dresden",R.drawable.ic_map),
                new ScrollViewData("Hannover",R.drawable.ic_map),
                new ScrollViewData("Nuernberg",R.drawable.ic_map),
                new ScrollViewData("Duisburg",R.drawable.ic_map),
                new ScrollViewData("Bochum",R.drawable.ic_map),
                new ScrollViewData("Wuppertal",R.drawable.ic_map),
                new ScrollViewData("Bielefeld",R.drawable.ic_map),
                new ScrollViewData("Bonn",R.drawable.ic_map),
                new ScrollViewData("Muenster",R.drawable.ic_map),
                new ScrollViewData("Mannheim",R.drawable.ic_map),
                new ScrollViewData("Augsburg",R.drawable.ic_map),
                new ScrollViewData("Wiesbaden",R.drawable.ic_map),
                new ScrollViewData("Gelsenkirchen",R.drawable.ic_map),
                new ScrollViewData("Moenchengladbach",R.drawable.ic_map),
                new ScrollViewData("Braunschweig",R.drawable.ic_map),
                new ScrollViewData("Chemnitz",R.drawable.ic_map),
                new ScrollViewData("Aachen",R.drawable.ic_map),
                new ScrollViewData("Kiel",R.drawable.ic_map),
                new ScrollViewData("Halle",R.drawable.ic_map),
                new ScrollViewData("Krefeld",R.drawable.ic_map),
                new ScrollViewData("Freiburg",R.drawable.ic_map),
                new ScrollViewData("Luebeck",R.drawable.ic_map),
                new ScrollViewData("Oberhausen",R.drawable.ic_map),
                new ScrollViewData("Erfurt",R.drawable.ic_map),
                new ScrollViewData("Mainz",R.drawable.ic_map),
                new ScrollViewData("Rostock",R.drawable.ic_map),
                new ScrollViewData("Kassel",R.drawable.ic_map),
                new ScrollViewData("Hagen",R.drawable.ic_map),
                new ScrollViewData("Saarbruecken",R.drawable.ic_map),
                new ScrollViewData("Hamm",R.drawable.ic_map),
                new ScrollViewData("Muelheim",R.drawable.ic_map),
                new ScrollViewData("Ludwigshafen",R.drawable.ic_map),
                new ScrollViewData("Potsdam",R.drawable.ic_map),
                new ScrollViewData("Leverkusen",R.drawable.ic_map),
                new ScrollViewData("Oldenburg",R.drawable.ic_map),
                new ScrollViewData("Osnabrueck",R.drawable.ic_map),
                new ScrollViewData("Herne",R.drawable.ic_map),
                new ScrollViewData("Karlsruhe",R.drawable.ic_map),
                new ScrollViewData("Neuss",R.drawable.ic_map),
                new ScrollViewData("Heidelberg",R.drawable.ic_map),
                new ScrollViewData("Darmstadt",R.drawable.ic_map),
                new ScrollViewData("Paderborn",R.drawable.ic_map),
                new ScrollViewData("Regensburg",R.drawable.ic_map),
                new ScrollViewData("Ingolstadt",R.drawable.ic_map),
                new ScrollViewData("Wuerzburg",R.drawable.ic_map),
                new ScrollViewData("Wolfsburg",R.drawable.ic_map),
                new ScrollViewData("Fuerth",R.drawable.ic_map),
                new ScrollViewData("Ulm",R.drawable.ic_map)
        };

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        ScrollViewAdapter mAdapter = new ScrollViewAdapter(itemsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Replace action bar with material design app bar from support library.
        // Menu items will automatically get added to this bar.
        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            getSupportActionBar().setTitle("Select city");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scroll_view, menu);
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
