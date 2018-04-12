package com.example.jingzehuang.parkstashmap.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Jingze HUANG on Apr.11, 2018.
 */

public class LocationList implements Parcelable{
    private int maxCapacity;
    private MyLocation lastLocation;
    private LinkedList<MyLocation> linkedList;
    private SparseArray<MyLocation> sparseArray;

    //Constructors
    public LocationList(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.linkedList = new LinkedList<>();
        sparseArray = new SparseArray<>(maxCapacity * 2);
    }

    public int size() {
        return linkedList.size();
    }

    public void initiate(List<MyLocation> list) {
        for (int index = 0, len = list.size(); index < len; index++) {
            MyLocation location = list.get(index);
            if (index == 0) {
                this.lastLocation = location;
            }
            add(location);
        }
    }

    public boolean add(MyLocation location) {
        if (location == null) {
            return false;
        }

        lastLocation = location;

        int locationHashCode = location.hashCode();
        MyLocation potentialObj = sparseArray.get(locationHashCode);
//        if (potentialObj != null && potentialObj.equals(location)) {
//      We suppose that each MyLocation's hashCode is different, when the exact location is different.
        if (potentialObj != null) {
            return false;
        }

        //Limit storage space.
        if (linkedList.size() >= maxCapacity) {
            int removedObjCode = linkedList.removeLast().hashCode();
            sparseArray.remove(removedObjCode);
        }

        sparseArray.append(locationHashCode, location);
        linkedList.addFirst(location);
        return true;
    }

    public LinkedList<MyLocation> getLinkedList() {
        return linkedList;
    }

    public MyLocation getLastLocation() {
        return this.lastLocation;
    }


    //Parcelable
    protected LocationList(Parcel in) {
        maxCapacity = in.readInt();
        lastLocation = in.readParcelable(MyLocation.class.getClassLoader());
    }

    public static final Creator<LocationList> CREATOR = new Creator<LocationList>() {
        @Override
        public LocationList createFromParcel(Parcel in) {
            return new LocationList(in);
        }

        @Override
        public LocationList[] newArray(int size) {
            return new LocationList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(maxCapacity);
        parcel.writeParcelable(lastLocation, i);
    }


}
