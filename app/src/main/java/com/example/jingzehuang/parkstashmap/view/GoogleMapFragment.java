package com.example.jingzehuang.parkstashmap.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingzehuang.parkstashmap.R;

import com.example.jingzehuang.parkstashmap.model.MyLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jingze HUANG on Apr.10, 2018.
 */

public class GoogleMapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener{
    @BindView(R.id.googleMapView) MapView googleMapView;

    public static final String BUNDLE_KEY_LIST = "locationLists";
    private static final float CAMERA_ZOOM_SMALL = 15.5f;
    private static final float CAMERA_ZOOM_LARGE = 12f;
    private Context mContext;
    private View view;
    private GoogleMap mGoogleMap;
    private ArrayList<MyLocation> locationList;

    public static GoogleMapFragment getInstance(ArrayList<MyLocation> locationList) {
        Bundle argument = new Bundle();
        argument.putParcelableArrayList(BUNDLE_KEY_LIST, locationList);
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
        locationList = getArguments().getParcelableArrayList(BUNDLE_KEY_LIST);

        googleMapView.onCreate(null);
        googleMapView.onResume();
        googleMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        MyLocation startupLocation = locationList.get(0);
        LatLng startupLatLng = startupLocation.getLatLng();
        googleMap.setMyLocationEnabled(true);
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startupLatLng));
        animateTo(startupLatLng);
        mGoogleMap.addMarker(new MarkerOptions().position(startupLatLng).title(startupLocation.getTitle()));

        Log.d("Raych", this + " Fragment: onMapReady() is called.");
    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    private boolean isReady() {
        return mGoogleMap != null;
    }

    public void animateTo(LatLng latLng) {
        animateTo(latLng, CAMERA_ZOOM_SMALL);
    }


    public void animateTo(LatLng latLng, float zoom) {
        if (!isReady()) {
            return;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(CAMERA_ZOOM_SMALL)
                .bearing(0f)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mGoogleMap.animateCamera(cameraUpdate);
    }
}
