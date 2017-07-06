package com.jasonette.builder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jasonette.builder.model.JasonModel;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import timber.log.Timber;

public class BuilderActivity extends AppCompatActivity {

    private static final long HEADER_ITEM_ID = 1;
    private static final long BODY_ITEM_ID = 2;

    private Toolbar toolbar;

    private JasonModel jasonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (jasonModel == null) {
            jasonModel = new JasonModel(getString(R.string.default_app_title));
        }

        setContentView(R.layout.activity_builder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(jasonModel.getHead().getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        navDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_builder, menu);
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    void navDrawer() {
        PrimaryDrawerItem headerItem = new PrimaryDrawerItem().
                withIdentifier(HEADER_ITEM_ID).withName(R.string.drawer_item_header);
        SecondaryDrawerItem actionsItem = new SecondaryDrawerItem().withName(R.string.drawer_item_actions);
        PrimaryDrawerItem bodyItem = new PrimaryDrawerItem().
                withIdentifier(BODY_ITEM_ID).withName(R.string.drawer_item_body);

        //create the drawer for building
        Drawer result = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .addDrawerItems(
                    headerItem,
                    actionsItem,
                    new DividerDrawerItem(),
                    bodyItem
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    Timber.d("clicked drawer item %s", drawerItem);
                    return true;
                }
            })
            .build();
    }
}
