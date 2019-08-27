package com.sandy.location;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.sandy.SandyLocationProvider;
import com.sandy.location_listener.SandyLocationListener;

class GPSUtils {

    private Context context;
    private SettingsClient settingsClient;
    private LocationSettingsRequest settingsRequest;
    private LocationManager manager;
    private LocationRequest locationRequest;
    private GPSStatusListener statusListener;
    static final int GPS_REQ_CODE = 122;

    GPSUtils(@NonNull Context context,LocationManager manager) {
        this.context = context;
        this.manager = manager;
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.settingsClient = LocationServices.getSettingsClient(context);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        builder.setAlwaysShow(true);
        this.settingsRequest = builder.build();
    }

    void turnGPSOn(final SandyLocationListener locationListener) {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (statusListener != null)
                statusListener.onStatus(true);
        } else {
            settingsClient.checkLocationSettings(settingsRequest)
                    .addOnSuccessListener((Activity) context,
                            new OnSuccessListener<LocationSettingsResponse>() {
                                @Override
                                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                    if (statusListener != null)
                                        statusListener.onStatus(true);
                                }
                            })
                    .addOnFailureListener((Activity) context, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int code = ((ApiException) e).getStatusCode();
                            switch (code) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult((Activity) context,GPS_REQ_CODE);
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        if(locationListener != null)
                                            locationListener.onFail(SandyLocationProvider.GPS_ERROR);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    if(locationListener != null)
                                        locationListener.onFail(SandyLocationProvider.CHANGE_UNAVAILABLE);
                                    break;

                            }
                        }
                    });
        }
    }

    void setStatusListener(GPSStatusListener listener) {
        statusListener = listener;
    }

    interface GPSStatusListener {
        void onStatus(boolean isEnabled);
    }
}
