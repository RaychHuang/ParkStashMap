package com.example.jingzehuang.parkstashmap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jingzehuang.parkstashmap.model.MyLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ModelUtils {
    private static String PREF_NAME = "models";
    private static final Gson GSON = new Gson();
//    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//    public static final Gson GSON_BITFINEX_EVENT = new GsonBuilder().registerTypeAdapter(MyLocation.class, new TypeAdapter<MyLocation>() {
//
//
//    @Override
//    public void write(JsonWriter out, MyLocation value) throws IOException {
//        if (value == null) {
//            out.nullValue();
//            return;
//        }
//        out.beginObject();
//        out.name("title"):
//        out.beginObject()
//        out.name("latLng").value(value.getLatLng());
//        out.name("channel").value(value.channel);
//        out.name("chanId").value(value.chanId);
//        out.name("symbol").value(value.symbol);
//        out.name("pair").value(value.pair);
//        out.name("version").value(value.version);
//        if (value.platform != null) {
//            out.name("platform").value(ModelUtils.toString(value.platform, new TypeToken<BitfinexManager.Event.PlatformInfo>(){}));
//        }
//        out.name("msg").value(value.msg);
//        out.name("errorCode").value(value.errorCode);
//        out.name("flags").value(value.flags);
//        if (value.coin != null) {
//            out.name("coin").value(ModelUtils.toString(value.coin, new TypeToken<Coin>(){}));
//        }
//        out.endObject();
//    }
//
//    @Override
//    public MyLocation read(JsonReader in) throws IOException {
//        return null;
//    }}).create();



    public static void save(Context context, String key, Object object) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        String jsonString = GSON.toJson(object);
        sp.edit().putString(key, jsonString).apply();
    }

    public static <T> T read(Context context, String key, TypeToken<T> typeToken) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        try {
            Log.d("Raych", sp.getString(key, ""));
            return GSON.fromJson(sp.getString(key, ""), typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toObject(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }

    public static <T> String toString(T object, TypeToken<T> typeToken) {
        return GSON.toJson(object, typeToken.getType());
    }

}
