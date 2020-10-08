# How to configure application

1. Edit `./urlConfig.properties`. Update `MOVIES_URL` with endpoint running API.
2. Edit `/app/src/main/res/xml/network_security_config.xml`. Add a line to create inclusion for your subdomain. 
    1. Ex: `<domain includeSubdomains="true">192.168.1.1</domain>`
    
# How to build and run app
1. Open project in android studio.
2. Select the `app` configuration and run.

You will need an Android VM or device to test on.

# Third-part Libraries

Hilt/Dagger - https://dagger.dev/hilt/

Dagger is Google's dependecy injection framework, and Hilt helps generate code for DI by filling boilerplate code, providing IDE code completion, and other tasks.

Android Room - https://developer.android.com/jetpack/androidx/releases/room

The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite. Part of Google's AndroidX Jetpack library. 

Retrofit - https://square.github.io/retrofit/

Square's HTTP client for robust and safe Http requests. This supports many asynchronous design patterns in Java and Kotlin. This framework is recommended by Google for use in application development of all sizes. The library is well tested and extensible for your needs.

Moshi - https://github.com/square/moshi

Square's Json serialization framework. This makes serializing Json to objects a breeze and works with Retrofit to serialize Json from Http requests into objects.

Glide - https://github.com/bumptech/glide

Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface. This is used by the project to load images into view and cache the results.
