# Sandy-Location-Provider


# How to

Add it in your root build.gradle at the end of repositories:
````
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
````

Add the dependency
````
	dependencies {
	        implementation 'com.github.sandeepkumarmishra354:Sandy-Location-Provider:Tag'
	}

````

# Now get location easily

initialize location provider
````
private SandyLocationProvider locationProvider = new SandyLocationProvider(this);
````
add location listener
````
locationProvider.setLocationListener(new SandyLocationListener() {
            @Override
            public void onSuccess(Location location) {
                //do whatever you want
            }
            @Override
            public void onFail(int status) {
                if(status == SandyLocationProvider.GPS_NOT_ENABLED) {
			//you did not enabled the gps
		}
		if(status == SandyLocationProvider.SECURITY_EXCEPTION) {
			//some security exception thrown
		}
		if(status == SandyLocationProvider.PERMISSION_DENIED) {
			//user denied the location permission
		}
		if(status == SandyLocationProvider.GPS_ERROR) {
			//gps error (very rare error)
		}
		if(status == SandyLocationProvider.CHANGE_UNAVAILABLE) {
			//gps error (very rare error)
		}
            }
        });
````

