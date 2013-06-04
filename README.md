# HomePi Application

This is a RESTful API for remote management of applications on distributed RaspberyPis .
    
## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $ java -cp target/classes:target/dependency/* com.meadowhawk.homepi.LocalMain

Note! This requires some System Properties being set to run.


-Denv.db.userid=postgres -Denv.db.password=Password1 
-Dgoogle_auth_client_id=gogole_client_id 
-Dgoogle_auth_client_secret=google_client_secret


## TODO:

* <strike>Add  Reg Pi Function</strike>
* Add supported apps update endpoint
* Check email notices
* Create zip app distribution
* Build Basic UI
* Enable user accounts
* Add basic data logging
* Add in API Doc gen

## Pi side TODOS

1. <strike>Make Reloader capable of downloading additional files/apps or zip files</strike>
2. Implement PiProfile registration and im alive ping
3. Add crypto
4. Add PiConfigs to help w id-ing  Managed apps, location, updatable files.. monitored log locations..etc
4. Add user sessions and multi-user design

## Next Step
* 1.1 Build CRUD EndPoints for Apps
* 1.2 Build App relationships to Registered Pi
* 
