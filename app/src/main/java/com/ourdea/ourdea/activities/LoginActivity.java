package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.calendar.CalendarSettings;
import com.ourdea.ourdea.dto.UserDto;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.UserResource;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mLoginButton;
    protected Button mGoRegisterButton;
    protected Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_login);

        // Enable calendar sync?
        if (!CalendarSettings.askedUserToEnableCalendarBefore(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Calendar");
            builder.setCancelable(false);
            builder.setMessage("Would you like Ourdea to sync tasks assigned to you to your calendar?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CalendarSettings.enableCalendar(LoginActivity.this);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CalendarSettings.disableCalendar(LoginActivity.this);
                }
            }).show();

            CalendarSettings.doNotAskUserToEnableCalendarAgain(LoginActivity.this);
        }

        // Hide action bar
        getActionBar().hide();

        mEmail = (EditText)findViewById(R.id.emailLoginTextBox);
        mPassword = (EditText)findViewById(R.id.passwordLoginTextBox);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mGoRegisterButton = (Button)findViewById(R.id.goRegisterButton);
        mContext = this;

        // Try to retrieve previous session information
        String email = ApiUtilities.Session.getEmail(LoginActivity.this);
        String password = ApiUtilities.Session.getPassword(LoginActivity.this);

        // Fill in details with previous session stuff
        if (!email.equals(ApiUtilities.Session.EMAIL_MISSING) && !password.equals(ApiUtilities.Session.PASSWORD_MISSING)) {
            mEmail.setText(email);
            mPassword.setText(password);
        }

        mGoRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegisterScreen = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goRegisterScreen);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                UserDto user = new UserDto(email, password);

                final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", false, false);
                UserResource.login(user, getApplicationContext(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                    String name = response.getString(getString(R.string.PROPERTY_USER_NAME));
                                    String password = response.getString("password");
                                    Log.d("TESTING", "name: " + name);

                                    ApiUtilities.Session.storeEmail(email, LoginActivity.this);
                                    ApiUtilities.Session.storePassword(password, LoginActivity.this);
                                    ApiUtilities.Session.storeName(name, LoginActivity.this);

                                    Intent goMainScreen = new Intent(LoginActivity.this, ProjectListActivity.class);
                                    startActivity(goMainScreen);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                                public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.d("TESTING", "error: " + error.getMessage());
                                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
