package com.example.jingzehuang.parkstashmap.model;

/**
 * Created by Jingze HUANG on Apr.10, 2018.
 */
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;

import java.util.Comparator;

public class MyLocation implements Parcelable{
    private String title;
    private LatLng latLng;
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

    @Override
    public int hashCode() {
//        Log.d("Raych", "hashCode() is Called");
        if (title == null) {
            return super.hashCode();
        }
        int hashCode = title.hashCode();
        if (latLng != null) {
            hashCode += Math.round(latLng.latitude * 100);
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        Log.e("Raych", "equals() is Called");
        if (!(obj instanceof MyLocation)) {
            return false;
        }

        LatLng latLng = ((MyLocation) obj).getLatLng();
        LatLng mLatLng = this.getLatLng();
        if (latLng == null || mLatLng == null) {
            return false;
        }

        final double DIF_ALLOWANCE = 0.00001;
        if (Math.abs(latLng.latitude - mLatLng.latitude) > DIF_ALLOWANCE
                || Math.abs(latLng.longitude - mLatLng.longitude) > DIF_ALLOWANCE) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return super.toString();
//    }

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
        private String title;
        private LatLng latLng;
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
