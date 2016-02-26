package com.graywolftechnoligies.tize0; /**
 * Created by neil on 11/23/15.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.graywolftechnoligies.tize0.R;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class Splash extends Activity {

    private final int Splash_Time=3000; //(ms)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


// Determine whether the current user is an anonymous user

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    // If user is anonymous, send the user to LoginActivity.class
                    Intent intent = new Intent(Splash.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    // If current user is NOT anonymous user
                    // Get current user data from Parse.com
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        // Send logged in users to ListEvents.class
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        // Send user to LoginActivity.class
                        Intent intent = new Intent(Splash.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, Splash_Time);
    }
}

