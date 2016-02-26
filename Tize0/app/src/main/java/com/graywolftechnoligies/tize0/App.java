package com.graywolftechnoligies.tize0;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by neil on 11/23/15.
 */
public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "YHvE8hQzcbqHfpDD29rO2hq0Xwn3fMOFm366KyGD", "YxtmXQBBjrxHMeEEmOFNzdks7VcJ1Ct1HPXhLxpj"); // Your Application ID and Client Key are defined elsewhere
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
