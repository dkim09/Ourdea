package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.UserResource;

public class DrawerActivity extends Activity {

    private String[] drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String drawerForActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerItems = getResources().getStringArray(R.array.drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerItems));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open_title,
                R.string.drawer_closed_title
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String drawerItem = drawerItems[position];

            // Don't do anything if we are currently in the activity
            if (drawerItem.equals(drawerForActivity)) {
                drawerLayout.closeDrawers();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            switch (drawerItem) {
                case "Tasks":
                    intent = new Intent(getApplicationContext(), TaskActivity.class);
                    break;
                case "Dashboard":
                    break;
                case "Chat":
                    intent = new Intent(getApplicationContext(), ChatActivity.class);
                    break;
                case "Meetings":
                    intent = new Intent(getApplicationContext(), MeetingActivity.class);
                    break;
                case "Map":
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    break;
                case "Logout":
                    UserResource.logout(getApplicationContext(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_SHORT).show();
                            ApiUtilities.Session.clearSession(getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Could not logout!", Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            startActivity(intent);
        }
    }

    public void setActivity(String activity) {
        drawerForActivity = activity;
    }
}
