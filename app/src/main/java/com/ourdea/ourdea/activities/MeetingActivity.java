package com.ourdea.ourdea.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ourdea.ourdea.R;

public class MeetingActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_meeting);
        super.onCreate(savedInstanceState);
        this.setActivity("Meetings");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add_meeting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
