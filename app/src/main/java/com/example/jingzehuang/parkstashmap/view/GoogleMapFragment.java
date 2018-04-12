package com.example.jingzehuang.parkstashmap.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.BounceInterpolator;

import com.example.jingzehuang.parkstashmap.R;

import com.example.jingzehuang.parkstashmap.model.LocationList;
import com.example.jingzehuang.parkstashmap.model.MyLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jingze HUANG on Apr.10, 2018.
 */

public class GoogleMapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener{

    @BindView(R.id.googleMapView) MapView googleMapView;

    public static final String BUNDLE_KEY_LIST = "locationLists";

    private static final String TAG = "Raych";

    private static final float CAMERA_ZOOM_SMALL = 15.5f;
    private static final float CAMERA_ZOOM_LARGE = 11f;

    private Context mContext;
    private View view;
    private GoogleMap mGoogleMap;
    private Handler mainThreadHandler;
    private LocationList locationList;
    private boolean isCameraMoving = true;

    public static GoogleMapFragment getInstance(LocationList locationList) {
        Bundle argument = new Bundle();
        argument.putParcelable(BUNDLE_KEY_LIST, locationList);
        GoogleMapFragment fragment = new GoogleMapFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Raych", this + " Fragment: onCreateView() is called.");
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("Raych", this + " Fragment: onViewCreated() is called.");
        locationList = getArguments().getParcelable(BUNDLE_KEY_LIST);

        googleMapView.onCreate(null);
        googleMapView.onResume();
        googleMapView.getMapAsync(this);

        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d("Raych", this + " Fragment: onMapReady() is called.");
        googleMap.setMyLocationEnabled(true);

        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);

//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startupLatLng));
        LinkedList<MyLocation> list = locationList.getLinkedList();
        for (int i = 0, len = locationList.size(); i < len; i++) {
            MyLocation location = list.get(i);
            if (i == 0) {
                LatLng startupLatLng = location.getLatLng();
                addMarker(startupLatLng, location.getTitle(), 0, 0);
                moveTo(startupLatLng);
            } else {
                addMarker(location.getLatLng(), location.getTitle(), 5, i * 300);
            }
        }

        Log.d("Raych", this + " Fragment: onMapReady() is called.");
    }

    @Override
    public void onCameraIdle() {
        Log.d(TAG,  "Camera is idle.");
        isCameraMoving = false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        Log.d(TAG,  "Map is clicked.");
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
//        Log.d(TAG,  "Map is long clicked.");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        bounceAnimation(marker, 0);
        Log.d(TAG, marker.toString() + " is clicked.");
        return true;
    }

    private boolean isReady() {
        return mGoogleMap != null;
    }

    public void addMarker(final LatLng latLng, final String title, int color, int delay) {
//        MarkerOptions marker = new MarkerOptions().position(startupLatLng).title(startupLocation.getTitle()).anchor(0.5f, 1f);

        final float colorIndex = (float) (color % 13) / 13;

        final float OFFSET_RATE = 9f;

        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isCameraMoving) {
                    mainThreadHandler.postDelayed(this, 1000);
                    return;
                }
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .anchor(0.5f, OFFSET_RATE)
                        .icon(BitmapDescriptorFactory.defaultMarker(colorIndex)));
                fallingAnimation(marker, OFFSET_RATE);
            }
        }, delay);
    }

    public void moveTo(LatLng latLng) {
        if (!isReady()) {
            return;
        }
        isCameraMoving = true;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(CAMERA_ZOOM_LARGE)
                .bearing(0f)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mGoogleMap.moveCamera(cameraUpdate);
    }

    public void animateTo(LatLng latLng) {
        animateTo(latLng, CAMERA_ZOOM_SMALL);
    }

    public void animateTo(LatLng latLng, float zoom) {
//        Log.d(TAG, "animateTo() is called.");
        if (!isReady()) {
            return;
        }
        isCameraMoving = true;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(CAMERA_ZOOM_SMALL)
                .bearing(0f)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void bounceAnimation(final Marker marker, int delay) {
        final Interpolator bounceInterpolator = new BounceInterpolator();

        final class BounceRunnable implements Runnable{

            private int upwardCount = 0;
            private final int MAX_UPWARD_COUNT = 9;

            private int bounceCount = 0;
            private final int MAX_BOUNCE_COUNT = 30;

            private final float OFFSET_RATE = 1.236f;

            @Override
            public void run() {
                float bounceRate;
                if (upwardCount <= MAX_UPWARD_COUNT) {
                    bounceRate = OFFSET_RATE * ((float) upwardCount++ / MAX_UPWARD_COUNT);
                    marker.setAnchor(0.5f, 1.0f + bounceRate);
                    mainThreadHandler.postDelayed(this, 16);
                } else {
                    float rate = (float) bounceCount++ / MAX_BOUNCE_COUNT;
                    bounceRate = OFFSET_RATE * (1 - bounceInterpolator.getInterpolation(rate));

                    if (bounceRate < 0.01f) {
                        marker.setAnchor(0.5f, 1.0f);
                        marker.setAlpha(0.25f);
                    } else {
                        marker.setAnchor(0.5f, 1.0f + bounceRate);
                        mainThreadHandler.postDelayed(this, 16);
                    }
                }
//                Log.d(TAG, "BounceRate: " + bounceRate);
            }
        }

        mainThreadHandler.postDelayed(new BounceRunnable(), delay);
    }

    private void fallingAnimation(final Marker marker, final float offsetRate) {
        final Interpolator fallingInterpolator = new AccelerateInterpolator(1f);

        final class FallingRunnable implements Runnable {
            private int downwardCount = 0;
            private final int MAX_DOWNWARD_COUNT = 26;

//            private final float OFFSET_RATE = 1.236f;

            @Override
            public void run() {
                if (downwardCount <= MAX_DOWNWARD_COUNT) {
                    float rate = (float) downwardCount++ / MAX_DOWNWARD_COUNT;
                    float fallingRate = offsetRate * (1 - fallingInterpolator.getInterpolation(rate));

                    if (fallingRate < 0.01f) {
                        marker.setAnchor(0.5f, 1.0f);
                    } else {
                        marker.setAnchor(0.5f, 1.0f + fallingRate);
                        mainThreadHandler.postDelayed(this, 16);
                    }
//                    Log.d(TAG, "BounceRate: " + fallingRate);
                }
            }
        }

        mainThreadHandler.post(new FallingRunnable());
    }
}
