package com.sandy.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sandy.location_listener.SandyLocationListener;

import static com.sandy.SandyLocationProvider.GPS_NOT_ENABLED;
import static com.sandy.SandyLocationProvider.PERMISSION_DENIED;
import static com.sandy.SandyLocationProvider.SECURITY_EXCEPTION;

public class SandyLocation {

    private Context context;//Application context
    private Activity activity;
    private LocationManager locationManager;
    private Criteria criteria;
    private LocationListener locationListener;
    private SandyLocationListener listener;
    private GPSUtils gpsUtils;
    private final int REQUEST_CODE = 1513;

    public SandyLocation(@NonNull Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.locationManager = (LocationManager) activity.
                getSystemService(Context.LOCATION_SERVICE);
    }

    public void onRequestPermissionsResult(int rqc, @NonNull String[] perms, @NonNull int[] grs) {
        if(rqc == REQUEST_CODE) {
            if(grs[0] == PackageManager.PERMISSION_GRANTED) {
                initGpsUtils();
                gpsUtils.turnGPSOn(listener);
            }
            else if(listener != null)
                listener.onFail(PERMISSION_DENIED);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == GPSUtils.GPS_REQ_CODE)
                listenForUpdate();
        } else if(resultCode == Activity.RESULT_CANCELED) {
            if(requestCode == GPSUtils.GPS_REQ_CODE) {
                if(listener != null)
                    listener.onFail(GPS_NOT_ENABLED);
            }
        }
    }

    private void initLocationListener() {
        if(locationListener == null) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationManager.removeUpdates(locationListener);
                    locationListener = null;
                    gpsUtils = null;
                    if(listener != null)
                        listener.onSuccess(location);
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    //
                }
                @Override
                public void onProviderEnabled(String provider) {
                    //
                }
                @Override
                public void onProviderDisabled(String provider) {
                    //
                }
            };
        }
    }

    private void initGpsUtils() {
        if(gpsUtils == null) {
            gpsUtils = new GPSUtils(activity,locationManager);
            gpsUtils.setStatusListener(new GPSUtils.GPSStatusListener() {
                @Override
                public void onStatus(boolean isEnabled) {
                    if(isEnabled)
                        listenForUpdate();
                }
            });
        }
    }

    public SandyLocation setLocationListener(SandyLocationListener listener) {
        this.listener = listener;
        return this;
    }

    public void fetchLocation() {
        if(criteria == null) {
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAltitudeRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(true);
            criteria.setBearingRequired(false);
            //API level 9 and up
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        }
        if(isPermissionGranted()) {
            initGpsUtils();
            gpsUtils.turnGPSOn(listener);
        } else
            requestForPermission();
    }

    private void listenForUpdate() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            initLocationListener();
            try {
                int MILLIS = 5000;
                int DIST = 5;
                locationManager.requestLocationUpdates(MILLIS, DIST, criteria,
                        locationListener, null);
            } catch (SecurityException e) {
                e.printStackTrace();
                if(listener != null)
                    listener.onFail(SECURITY_EXCEPTION);
            }
        } else {
            if(listener != null) {
                listener.onFail(GPS_NOT_ENABLED);
            }
        }
    }

    private void requestForPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(activity,perms,REQUEST_CODE);
    }

    private boolean isPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(context,Manifest.permission
                .ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
