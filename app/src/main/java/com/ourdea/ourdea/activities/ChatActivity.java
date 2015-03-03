package com.ourdea.ourdea.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.fragments.ChatFragment;

public class ChatActivity extends FragmentActivity {

    private ChatFragment fragment_chat;
    private String mEmail;
    private String mName;
    private int mProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent givenIntent = getIntent();
        mProjectId = givenIntent.getIntExtra(getString(R.string.PROPERTY_PROJECT_ID), 0);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PROPERTY_NAME), Context.MODE_PRIVATE);
        mEmail = prefs.getString(getString(R.string.PROPERTY_EMAIL), "");
        mName = prefs.getString(getString(R.string.PROPERTY_USER_NAME), "");
        mProjectId = 1;
        fragment_chat = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        fragment_chat.initializeChat(mProjectId, mEmail, mName);
    }
}
