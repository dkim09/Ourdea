package com.ourdea.ourdea.dto;

import android.content.Context;

import com.ourdea.ourdea.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Comment {

    private int mCommentId;
    private int mProjectId;
    private boolean mIsMyComment;
    private String mContent;
    private String mTime;
    private Boolean mIsLoading;

    public Comment (){

    }

    public Comment (int projectId, int commentId, boolean isMyComment, String content, int minutesAgo){
        mProjectId = projectId;
        mCommentId = commentId;
        mIsMyComment = isMyComment;
        mContent = content;
        mTime = getTimeFromMinutesAgo(minutesAgo);
        mIsLoading = false;
    }

    public Comment (Context context, JSONObject jsonObject) throws JSONException {
        setCommentId(jsonObject.getInt(context.getString(R.string.PROPERTY_COMMENT_ID)));
        setProjectId(jsonObject.getInt(context.getString(R.string.PROPERTY_PROJECT_ID)));
        setMyComment(jsonObject.getBoolean(context.getString(R.string.PROPERTY_COMMENT_MINE)));
        setContent(jsonObject.getString(context.getString(R.string.PROPERTY_COMMENT_CONTENT)));
        if (jsonObject.has(context.getString(R.string.PROPERTY_COMMENT_MINUTES_AGO))){
            setTime(getTimeFromMinutesAgo(jsonObject.getInt(context.getString(R.string.PROPERTY_COMMENT_MINUTES_AGO))));
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
}
