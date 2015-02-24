package com.ourdea.ourdea.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.fragments.ChatFragment;

public class ChatActivity extends FragmentActivity {

    private ChatFragment fragment_chat;
    private String mEmail;
    private int mProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent givenIntent = getIntent();
        mEmail = givenIntent.getStringExtra(getString(R.string.PROPERTY_EMAIL));
        mProjectId = givenIntent.getIntExtra(getString(R.string.PROPERTY_PROJECT_ID), 0);

        fragment_chat = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        fragment_chat.initializeChat(mProjectId, mEmail);
    }
}
