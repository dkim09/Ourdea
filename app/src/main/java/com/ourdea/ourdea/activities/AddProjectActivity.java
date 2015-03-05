package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class AddProjectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        final Context context = this;

        final EditText projectName = (EditText) findViewById(R.id.create_project_name);
        final EditText projectPass = (EditText) findViewById(R.id.create_project_password);
        final Button createProjectButton = (Button) findViewById(R.id.create_project);

        createProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameValue = projectName.getText().toString();
                final String passValue = projectPass.getText().toString();

                ProjectApi.create(nameValue, passValue, context,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(AddProjectActivity.this, "Project created", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddProjectActivity.this);
                                alertDialogBuilder.setTitle("Project created");
                                try {
                                    alertDialogBuilder.setMessage("Share this information with others to let them join your project:\n\nProject ID: " + response.getString("projectId") + "\nPassword: " + response.getString("password"));
                                    alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent goMainScreen = new Intent(AddProjectActivity.this, DashboardActivity.class);
                                            startActivity(goMainScreen);
                                        }
                                    }).show();
                                } catch (Exception e) {
                                    Log.d("SERVER_ERROR", "can't parse project");
                                }
                                Log.d("SERVER_SUCCESS", "Project Created");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(AddProjectActivity.this, "Project could not be created", Toast.LENGTH_SHORT).show();
                                Log.d("SERVER_ERROR", "Project could not be created");
                            }
                        });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_project, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
