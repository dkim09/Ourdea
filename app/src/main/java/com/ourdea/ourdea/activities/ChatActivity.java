package com.ourdea.ourdea.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.fragments.ChatFragment;
import com.ourdea.ourdea.resources.ApiUtilities;

public class ChatActivity extends FragmentActivity {

    private ChatFragment fragment_chat;
    private String mEmail;
    private String mName;
    private int mProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mProjectId = ApiUtilities.Session.getProjectId(getApplicationContext()).intValue();
        mEmail = ApiUtilities.Session.getEmail(getApplicationContext());
        mName = ApiUtilities.Session.getName(getApplicationContext());
        fragment_chat = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        fragment_chat.initializeChat(mProjectId, mEmail, mName);
    }
}
