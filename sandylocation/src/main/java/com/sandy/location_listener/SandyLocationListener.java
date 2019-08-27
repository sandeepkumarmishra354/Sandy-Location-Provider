package com.sandy.location_listener;

import android.location.Location;

public interface SandyLocationListener {
    void onSuccess(Location location);
    void onFail(int status);
}
