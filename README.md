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
````[java]
private SandyLocationProvider locationProvider = new SandyLocationProvider(this);
````

