package com.ourdea.ourdea.dto;

import android.content.Context;

import com.ourdea.ourdea.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentDto {

    private int mCommentId;
    private int mProjectId;
    private boolean mIsMyComment;
    private String mEmail;
    private String mName;
    private String mContent;
    private String mTime;
    private Boolean mIsLoading;

    public CommentDto(){

    }

    public CommentDto(int projectId, int commentId, boolean isMyComment, String name, String content, int minutesAgo){
        mProjectId = projectId;
        mCommentId = commentId;
        mIsMyComment = isMyComment;
        mName = name;
        mContent = content;
        mTime = getTimeFromMinutesAgo(minutesAgo);
        mIsLoading = false;
    }

    public CommentDto(Context context, String email, JSONObject jsonObject) throws JSONException {
        JSONObject user = jsonObject.getJSONObject("user");
        setCommentId(jsonObject.getInt(context.getString(R.string.PROPERTY_COMMENT_ID)));
        setProjectId(jsonObject.getInt(context.getString(R.string.PROPERTY_PROJECT_ID)));
        setEmail(user.getString(context.getString(R.string.PROPERTY_EMAIL)));
        setMyComment(email.equals(mEmail));
        setName(user.getString(context.getString(R.string.PROPERTY_USER_NAME)));
        setContent(jsonObject.getString(context.getString(R.string.PROPERTY_COMMENT_CONTENT)));
        if (jsonObject.has(context.getString(R.string.PROPERTY_COMMENT_TIME))){
            setTime(getTimeFromMinutesAgo(jsonObject.getInt(context.getString(R.string.PROPERTY_COMMENT_TIME))));
        }
        mIsLoading = false;
    }

    private String getTimeFromMinutesAgo (int minutesAgo){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, (-1 * minutesAgo));
        if (minutesAgo <= 1440) { // same day
            SimpleDateFormat date_format = new SimpleDateFormat("HH:mm");
            return date_format.format(calendar.getTime());
        } else {
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd");
            return date_format.format(calendar.getTime());
        }
    }

    public int getCommentId() {
        return mCommentId;
    }

    public void setCommentId(int commentId) {
        mCommentId = commentId;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public void setProjectId(int projectId) {
        mProjectId = projectId;
    }

    public boolean isMyComment() {
        return mIsMyComment;
    }

    public void setMyComment(boolean isMyComment) {
        mIsMyComment = isMyComment;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public Boolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(Boolean isLoading) {
        mIsLoading = isLoading;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}
