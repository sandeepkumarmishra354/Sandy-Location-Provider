# Sandy-Location-Provider

A location provider that helps you to get the device current location.
You do not need to add those location permissions anymore in your manifest (bcz it adds those permission in manifest for you already).

It automatically prompts the user for location permission and shows a dialog to enable the GPS (if it not enabled)

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
	        implementation 'com.github.sandeepkumarmishra354:Sandy-Location-Provider:-SNAPSHOT'
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
in your activity simply override these methods (most important)
````
    @Override
    public void onRequestPermissionsResult(int rqc, @NonNull String[] perms, @NonNull int[] grs) {
        super.onRequestPermissionsResult(rqc, perms, grs);
        locationProvider.onRequestPermissionsResult(rqc,perms,grs);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        locationProvider.onActivityResult(requestCode,resultCode,data);
    }
````
# Last Step

just call that function
``locationProvider.getLocation();``
