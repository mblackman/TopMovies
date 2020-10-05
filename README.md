# How to configure application

1. Edit `./urlConfig.properties`. Update `MOVIES_URL` with endpoint running API.
2. Edit `/app/src/main/res/xml/network_security_config.xml`. Add a line to create inclusion for your subdomain. 
    1. Ex: `<domain includeSubdomains="true">192.168.1.1</domain>`
    
# How to build and run app
1. Open project in android studio.
2. Select the `app` configuration and run.

You will need a Android VM or device to test on.
