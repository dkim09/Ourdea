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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends Activity {

    String loginURL = "http://192.168.0.21:9000/login";
    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mLoginButton;
    protected Button mGoRegisterButton;
    protected Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText)findViewById(R.id.emailLoginTextBox);
        mPassword = (EditText)findViewById(R.id.passwordLoginTextBox);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mGoRegisterButton = (Button)findViewById(R.id.goRegisterButton);
        mContext = this;

        mGoRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goRegisterScreen = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goRegisterScreen);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                HashMap<String, String> loginInfo = new HashMap<String, String>();
                loginInfo.put("email", email);
                loginInfo.put("password", password);

                JSONObject json = new JSONObject(loginInfo);
                JsonObjectRequest request = new JsonObjectRequest
                        (Request.Method.POST, loginURL, json, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                Intent goMainScreen = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(goMainScreen);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TESTING", "error: " + error.getMessage());
                                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                            }
                        });
                RequestQueueSingleton.getInstance(mContext).addToRequestQueue(request);

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
