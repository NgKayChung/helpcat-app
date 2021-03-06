package com.app.my.helpcatapp;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;

// TopActivity is an activity which will not display anything to the screen
// it is an activity that runs every time application is opened
public class TopActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        SharedPreferences savedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // if no saved preferences - indicates not logged in, to login page
        if(savedPrefs.getString("KEY_ID", null) == null) {

            Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else { // otherwise, to homepage
            Intent intent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}