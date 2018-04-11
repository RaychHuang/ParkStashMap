package com.example.jingzehuang.parkstashmap.model;

/**
 * Created by Jingze HUANG on Apr.10, 2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MyLocation implements Parcelable{
    private LatLng latLng;
    private String title;
    private String note;

    //Constructor
    private MyLocation(Builder builder) {
        this.latLng = builder.latLng;
        this.title = builder.title;
        this.note = builder.note;
    }
    
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    //Parcelable
    protected MyLocation(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        note = in.readString();
    }

    public static final Creator<MyLocation> CREATOR = new Creator<MyLocation>() {
        @Override
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        @Override
        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
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
    
    public static class Builder {
        private LatLng latLng;
        private String title;
        private String note;

        public Builder setLatLng(LatLng latLng) {
            this.latLng = latLng;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setNote(String note) {
            this.note = note;
            return this;
        }
        
        public MyLocation build() {
            return new MyLocation(this);
        }
    }
}
