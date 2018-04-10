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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jingze HUANG on Apr.10, 2018.
 */

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback{
    @BindView(R.id.googleMapView) MapView googleMapView;
    private Context mContext;
    private View view;
    private GoogleMap mGoogleMap;


    public static GoogleMapFragment getInstance() {
        GoogleMapFragment fragment = new GoogleMapFragment();
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
        googleMapView.onCreate(null);
        googleMapView.onResume();
        googleMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng SANJOSE = new LatLng(37.33, -121.89);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        mGoogleMap.addMarker(new MarkerOptions().position(SANJOSE).title("Marker in San Jose"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(SANJOSE));
        Log.d("Raych", this + " Fragment: onMapReady() is called.");
    }
}
