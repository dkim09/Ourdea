package com.ourdea.ourdea.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.adapters.ChatAdapter;
import com.ourdea.ourdea.api.ProjectApi;
import com.ourdea.ourdea.dto.Comment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ChatFragment extends ListFragment implements View.OnClickListener {

    private Context mContext;
    private EditText txt_comment;
    private ImageButton btn_send;

    private ChatAdapter mAdapterChat;
    private String mEmail;
    private String mName;
    private int mProjectId;
    private int mLeftBubble;
    private int mRightBubble;
    private ArrayList<Comment> mComments;

    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TESTING", "GOT CHAT MESSAGE: " + intent.toString());
            String type = intent.getStringExtra(getString(R.string.PROPERTY_TYPE));
            if (type != null && type.equals(getString(R.string.PROPERTY_CHAT)) &&
                    mProjectId == Integer.parseInt(intent.getStringExtra(getString(R.string.PROPERTY_PROJECT_ID)))){
                if (!mEmail.equals(intent.getStringExtra(getString(R.string.PROPERTY_EMAIL)))) {
                    int commentId = Integer.parseInt(intent.getStringExtra(getString(R.string.PROPERTY_COMMENT_ID)));
                    String content = intent.getStringExtra(getString(R.string.PROPERTY_COMMENT_CONTENT));
                    String name = intent.getStringExtra(getString(R.string.PROPERTY_USER_NAME));
                    Comment newComment = new Comment (mProjectId, commentId, false, name, content, 0);
                    addNewCommentToChat (newComment);
                }
                abortBroadcast();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        txt_comment = (EditText) rootView.findViewById(R.id.txt_comment);
        btn_send = (ImageButton) rootView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();

        mComments = new ArrayList<Comment>();
        mAdapterChat = new ChatAdapter(mContext, mComments, mLeftBubble, mRightBubble);
        setListAdapter(mAdapterChat);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btn_send)){
            String content = txt_comment.getText().toString();
            if (content.trim().length() > 0){
                Comment newComment = new Comment(mProjectId, 0, true, mName, txt_comment.getText().toString(), 0);
                sendComment(newComment);
                txt_comment.setText("");
            }
        }
    }

    public void initializeChat (int projectId, String email, String name){
        mProjectId = projectId;
        mEmail = email;
        mName = name;
        mLeftBubble = R.drawable.img_chat_left;
        mRightBubble = R.drawable.img_chat_right;
    }

    private void loadChat (){
        ProjectApi.getChat(mProjectId, this.getActivity(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    mComments.clear();
                    int length = response.length();
                    for (int i = 0; i < length; i++){
                        Comment newComment = new Comment (mContext, response.getJSONObject(i));
                        mComments.add(newComment);
                    }
                    mAdapterChat.notifyDataSetChanged();
                    txt_comment.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "error: " + error.getMessage());
            }
        });
    }

    private void sendComment (final Comment comment){
        mComments.add(comment);
        mAdapterChat.notifyDataSetChanged();
        ProjectApi.comment(mProjectId, comment.getContent(), this.getActivity(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.d("TESTING", "response from server: " + response.toString());
                comment.setIsLoading(false);
                mAdapterChat.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        Map<String, String> params = new HashMap<String, String>();
//        params.put(mContext.getString(R.string.PROPERTY_CHAT_ID), "" + mChatId);
//        params.put(mContext.getString(R.string.PROPERTY_CHAT_TYPE), "" + mChatType);
//        params.put(mContext.getString(R.string.PROPERTY_FB_ID), mFbId);
//        params.put(mContext.getString(R.string.PROPERTY_COMMENT_OWNER), ""+comment.isOwnerComment());
//        params.put(mContext.getString(R.string.PROPERTY_COMMENT_LETTER), ""+comment.getLetter());
//        params.put(mContext.getString(R.string.PROPERTY_COMMENT_COLOR), ""+comment.getColor());
//        params.put(mContext.getString(R.string.PROPERTY_COMMENT_CONTENT), comment.getContent());
//        JsonObjectParamsRequest jsObjRequest = new JsonObjectParamsRequest
//                (com.android.volley.Request.Method.POST, getString(R.string.server_comment), params, new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (isAdded()){
//                            try {
//                                if (!response.has(mContext.getString(R.string.PROPERTY_ERROR))){
//                                    int commentId = Integer.parseInt(response.getString(mContext.getString(R.string.PROPERTY_COMMENT_ID)));
//                                    int letter = Integer.parseInt(response.getString(mContext.getString(R.string.PROPERTY_COMMENT_LETTER)));
//                                    int color = Integer.parseInt(response.getString(mContext.getString(R.string.PROPERTY_COMMENT_COLOR)));
//                                    if (mColor == -1 && mLetter == -1){ // if first comment, so waited to get assigned icon
//                                        mLetter = letter;
//                                        mColor = color;
//                                        comment.setCommentId(commentId);
//                                        comment.setLetter(letter);
//                                        comment.setColor(color);
//                                        comment.setIsLoading(false);
//                                        addNewCommentToChat(comment);
//                                    } else {
//                                        mLetter = letter;
//                                        mColor = color;
//                                        comment.setCommentId(commentId);
//                                        comment.setLetter(letter);
//                                        comment.setColor(color);
//                                        comment.setIsLoading(false);
//                                        mAdapterComment.notifyDataSetChanged();
//                                    }
//                                    scrollToBottom (true);
//                                } else {
//                                    Log.d("TESTING", "Error posting comment");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                mComments.remove(comment);
//                                mAdapterComment.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (isAdded()) {
//                            showToast(getString(R.string.error_no_internet));
//                            error.printStackTrace();
//                            mComments.remove(comment);
//                            mAdapterComment.notifyDataSetChanged();
//                        }
//                    }
//                });
//        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    private void addNewCommentToChat (Comment comment){
        mComments.add(comment);
        mAdapterChat.notifyDataSetChanged();
    }

    private void showToast (String message){
        Toast toast;
        toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onPause() {
        if (isAdded()){
            mContext.unregisterReceiver(chatReceiver);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (isAdded()){
            loadChat();
            IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
            filter.setPriority(1);
            mContext.registerReceiver(chatReceiver, filter);
        }

        super.onResume();
    }
}
