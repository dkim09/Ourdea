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


public class RegisterActivity extends Activity {

    String createUserURL = "http://192.168.0.21:9000/user";
    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mRegisterButton;
    protected Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUsername = (EditText)findViewById(R.id.usernameRegisterEditText);
        mEmail = (EditText)findViewById(R.id.emailRegisterEditText);
        mPassword = (EditText)findViewById(R.id.passwordRegisterEditText);
        mRegisterButton = (Button)findViewById(R.id.registerButton);
        mContext = this;

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String name =  mUsername.getText().toString().trim();
                String password =  mPassword.getText().toString().trim();

                if (checkInputs(email,password)) {

                    HashMap<String, String> userInfo = new HashMap<String, String>();
                    userInfo.put("email", email);
                    userInfo.put("name",name);
                    userInfo.put("password", password);
                    userInfo.put("gcmId", "Test");

                    JSONObject json = new JSONObject(userInfo);
                    JsonObjectRequest request = new JsonObjectRequest
                            (Request.Method.POST, createUserURL, json, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TESTING", "response: " + response.toString());
                                    Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                    Intent goLoginScreen = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(goLoginScreen);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("TESTING", "error: " + error.getMessage());
                                    Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_LONG).show();

                                }
                            });
                    RequestQueueSingleton.getInstance(mContext).addToRequestQueue(request);
                }
            }
        });
    }

    public boolean checkInputs(String email, String password){
        boolean isValid = true;
        String msg = "";
        if (!(email.contains("@") && email.contains("."))){
            msg += "Please enter a valid email address\n";

            isValid = false;
        }

        if (password.length() < 6){
            msg += "Password should be at least 6 characters long";
            isValid = false;
        }
        if (!isValid)
            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
        return isValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
