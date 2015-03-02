package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.api.ProjectApi;

import org.json.JSONObject;

public class ProjectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        final Context context = this;

        final EditText projectId = (EditText) findViewById(R.id.join_project_id);
        final EditText projectPass = (EditText) findViewById(R.id.join_project_password);
        final Button joinProjectButton = (Button) findViewById(R.id.join_project);

        joinProjectButton.setOnClickListener(new View.OnClickListener() {
            final String idValue = projectId.getText().toString();
            final String passValue = projectPass.getText().toString();
            @Override
            public void onClick(View v) {
                    ProjectApi.join(idValue, passValue, context,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                             Toast.makeText(ProjectActivity.this, "Project Joined", Toast.LENGTH_SHORT).show();
                             finish();
                             Log.d("SERVER_SUCCESS", "Project Joined");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("SERVER_ERROR", "Project could not be found");
                                }
                            });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_project) {
            Intent intent = new Intent(this, AddProjectActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
