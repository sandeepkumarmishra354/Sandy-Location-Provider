package com.sandy;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.sandy.location.SandyLocation;
import com.sandy.location_listener.SandyLocationListener;

public class SandyLocationProvider {

    public static final int SECURITY_EXCEPTION = 101;
    public static final int PERMISSION_DENIED = 102;
    public static final int GPS_ERROR = 103;
    public static final int CHANGE_UNAVAILABLE = 104;
    public static final int GPS_NOT_ENABLED = 105;

    private SandyLocation sandyLocation;

    public SandyLocationProvider(@NonNull Activity activity) {
        this.sandyLocation = new SandyLocation(activity);
    }

    public void onRequestPermissionsResult(int rqc, @NonNull String[] perms, @NonNull int[] grs) {
        sandyLocation.onRequestPermissionsResult(rqc,perms,grs);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        sandyLocation.onActivityResult(requestCode,resultCode,data);
    }

    public void getLocation() {
        sandyLocation.fetchLocation();
    }

    public SandyLocation setLocationListener(SandyLocationListener listener) {
        return sandyLocation.setLocationListener(listener);
    }
}
