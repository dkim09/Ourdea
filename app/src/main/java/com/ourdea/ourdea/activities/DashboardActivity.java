package com.ourdea.ourdea.activities;

import android.os.Bundle;

import com.ourdea.ourdea.R;


public class DashboardActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.setActivity("Dashboard");
        super.onCreate(savedInstanceState);
    }

}
