package com.app.my.firstapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class HomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences savedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        if(savedPrefs.getString("KEY_ID", null) == null) {
            //if no saved preferences / records
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            //preferences existed
            Intent intent = new Intent(this, Scanner.class);
            startActivity(intent);
        }
    }
}