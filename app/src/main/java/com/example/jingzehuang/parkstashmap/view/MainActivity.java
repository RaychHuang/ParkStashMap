package com.example.jingzehuang.parkstashmap.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.jingzehuang.parkstashmap.R;
import com.example.jingzehuang.parkstashmap.model.LocationList;
import com.example.jingzehuang.parkstashmap.model.MyLocation;
import com.example.jingzehuang.parkstashmap.utils.ModelUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.main_frame) FrameLayout mainFrameLayout;
    @BindView(R.id.action_bar) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;

    private static final String TAG = "Raych";

    private static final String SP_LOCATIONS_KEY = "places";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;

    private final int LOCATION_LIST_CAPACITY = 10;

    private GoogleMapFragment mapFragment;
//    private ArrayList<MyLocation> locationList;
    private LocationList mdLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        locationList = loadData();
        mdLocationList = loadData();
        setupUI();
        Log.e(TAG, "Size(): " + mdLocationList.size());
    }

    @Override
    protected void onPause() {
        super.onPause();
//        ModelUtils.save(getApplicationContext(),
//                SP_LOCATIONS_KEY, locationList);
        ModelUtils.save(getApplicationContext(),
                SP_LOCATIONS_KEY, mdLocationList.getLinkedList());
        Log.i(TAG, this + " MainActivity.onPause(): Location list has been saved. Data size: "
                + mdLocationList.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String resultTitle = place.getName().toString();
                LatLng resultLatLng = place.getLatLng();
                Log.i(TAG, "Place: " + resultTitle + " " + resultLatLng);

                MyLocation targetLocation = new MyLocation.Builder()
                        .setTitle(resultTitle)
                        .setLatLng(resultLatLng)
                        .build();

                if (mdLocationList.add(targetLocation)) {
                    mapFragment.addMarker(resultLatLng, resultTitle, 0, 400);
                }
                mapFragment.animateTo(targetLocation.getLatLng());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupUI() {
        setupActionBar();
        addFragment();
        setupFab();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mapFragment = GoogleMapFragment.getInstance(mdLocationList);
        transaction.add(mainFrameLayout.getId(), mapFragment);
        transaction.commit();
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                appBarLayout.setExpanded(true);
                startSearchActivity();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void startSearchActivity() {
        try {

            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

//    private ArrayList<MyLocation> loadData() {
//        ArrayList<MyLocation> data = ModelUtils.read(getApplicationContext(), SP_LOCATIONS_KEY, new TypeToken<ArrayList<MyLocation>>(){});
//
//        if (data == null) {
//            data = new ArrayList<>(LOCATION_LIST_CAPACITY + 3);
//            MyLocation sanjose = new MyLocation.Builder()
//                    .setTitle("San Jose")
//                    .setLatLng(new LatLng(37.3382, -121.8863))
//                    .build();
//            data.add(sanjose);
//        }
//
//        return data;
//    }

    private LocationList loadData() {
        LinkedList<MyLocation> list = ModelUtils.read(getApplicationContext(), SP_LOCATIONS_KEY,
                new TypeToken<LinkedList<MyLocation>>(){});

        Log.d(TAG, "loadData(): " + (list == null));

        if (list == null ){
            list = mockData();
        }

        LocationList locationList = new LocationList(LOCATION_LIST_CAPACITY);
        locationList.initiate(list);
        return locationList;
    }

    private LinkedList<MyLocation> mockData() {
        LinkedList<MyLocation> list = new LinkedList<>();

        MyLocation sanjose = new MyLocation.Builder()
                .setTitle("San Jose")
                .setLatLng(new LatLng(37.3382082,-121.8863286))
                .build();
        list.add(sanjose);

        return list;
    }

    private void setCameraFocus() {

    }
}
