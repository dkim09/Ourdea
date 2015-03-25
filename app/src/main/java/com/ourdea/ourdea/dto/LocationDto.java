package com.ourdea.ourdea.dto;

import android.content.Context;

import com.ourdea.ourdea.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationDto {

    private String mEmail;
    private String mName;
    private double mLatitude;
    private double mLongitude;

    public LocationDto(){

    }

    public LocationDto(Context context, JSONObject jsonObject) throws JSONException {
        JSONObject user = jsonObject.getJSONObject("user");
        setEmail(user.getString(context.getString(R.string.PROPERTY_EMAIL)));
        setName(user.getString(context.getString(R.string.PROPERTY_USER_NAME)));
        setLatitude(jsonObject.getDouble(context.getString(R.string.PROPERTY_LATITUDE)));
        setLongitude(jsonObject.getDouble(context.getString(R.string.PROPERTY_LONGITUDE)));

    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
