package com.example.jingzehuang.parkstashmap.model;

/**
 * Created by Jingze HUANG on Apr.10, 2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Location implements Parcelable{
    private LatLng latLng;
    private String title;
    private String note;

    //Parcelable
    protected Location(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        note = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(latLng, i);
        parcel.writeString(title);
        parcel.writeString(note);
    }
}
