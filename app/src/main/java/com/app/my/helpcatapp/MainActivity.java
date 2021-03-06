package com.app.my.helpcatapp;

import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.*;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private boolean isBackPressed = false;

    private ImageView button1_img, button2_img, button3_img;
    private TextView button1_txt, button2_txt, button3_txt;

    private FloatingActionButton fab;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // initialize function buttons
        button1_img = (ImageView) findViewById(R.id.button1_img);
        button1_txt = (TextView) findViewById(R.id.button1_text);
        button2_img = (ImageView) findViewById(R.id.button2_img);
        button2_txt = (TextView) findViewById(R.id.button2_text);
        button3_img = (ImageView) findViewById(R.id.button3_img);
        button3_txt = (TextView) findViewById(R.id.button3_text);

        pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        final String userType = pref.getString("KEY_TYPE", null);

        // determine buttons to display
        if(userType.equals("student")) { // if user is a student
            button1_img.setImageResource(R.drawable.ic_school_black_24dp);
            button1_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button1_txt.setText("SUBJECT ENROLL");

            button1_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, StudentEnrollActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            button2_img.setImageResource(R.drawable.ic_event_available_black_24dp);
            button2_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button2_txt.setText("ATTENDANCE");

            button2_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, StudentAttendanceActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            button3_img.setImageResource(R.drawable.ic_menu_manage);
            button3_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button3_txt.setText("CHANGE PASSWORD");

            button3_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, UserChangePasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            // initialize mailbox button
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, StudentMessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        } else if(userType.equals("lecturer")) { // if user is lecturer
            button1_img.setImageResource(R.drawable.ic_school_black_24dp);
            button1_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button1_txt.setText("CLASSES");

            button1_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LecturerMessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            button2_img.setImageResource(R.drawable.ic_menu_camera);
            button2_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button2_txt.setText("ATTENDANCE");

            button2_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LecturerAttendanceActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            button3_img.setImageResource(R.drawable.ic_menu_manage);
            button3_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button3_txt.setText("CHANGE PASSWORD");

            button3_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, UserChangePasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        } else { // otherwise, user is admin
            button1_img.setImageResource(R.drawable.ic_event_available_black_24dp);
            button1_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button1_txt.setText("SUBJECT APPROVAL");

            button1_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AdminEnrollApprovalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            button2_img.setImageResource(R.drawable.ic_menu_manage);
            button2_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button2_txt.setText("RESET USER PASSWORD");

            button2_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AdminResetPasswordApprovalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            button3_img.setImageResource(R.drawable.ic_school_black_24dp);
            button3_img.setBackground(getDrawable(R.drawable.buttonbackground));
            button3_txt.setText("REGISTER NEW STUDENT");

            button3_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AdminRegisterStudentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        }

        // initialize image slider
        // set slider dots at the bottom part of the slider
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.sliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View sidebar_view = navigationView.getHeaderView(0);

        // set user's info at sidebar
        final TextView name_textView = (TextView) sidebar_view.findViewById(R.id.name_textView);
        name_textView.setText(pref.getString("KEY_NAME", "Name") + " (" + pref.getString("KEY_ID", "ID") + ")");

        final TextView email_textView = (TextView) sidebar_view.findViewById(R.id.email_textView);
        email_textView.setText(pref.getString("KEY_EMAIL", "Email Address"));
    }

    public class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if(viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed) {
                super.onBackPressed();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return;
            }

            this.isBackPressed = true;
            Toast.makeText(this, "Press the BACK button once again to close the application", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle logout process here
        int item_id = item.getItemId();
        item.setChecked(true);
        if (item_id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();

            SharedPreferences currentPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = currentPrefs.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}