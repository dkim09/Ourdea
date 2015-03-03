package com.ourdea.ourdea.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.Comment;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<Comment> mComments;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mLeftBubble;
    private int mRightBubble;

    class ViewHolder {
        LinearLayout layout_comment;
        TextView txt_name;
        TextView txt_comment;
        TextView txt_time;
        ProgressBar prg_loading;
    }

    public ChatAdapter (Context context, ArrayList<Comment> comments, int leftBubble, int rightBubble){
        mContext = context;
        mComments = comments;
        mLeftBubble = leftBubble;
        mRightBubble = rightBubble;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_comment, null);

            viewHolder.layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_comment);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txt_comment = (TextView) convertView.findViewById(R.id.txt_comment);
            viewHolder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
            viewHolder.prg_loading = (ProgressBar) convertView.findViewById(R.id.prg_loading);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.layout_comment.removeAllViews();
        Comment comment = mComments.get(position);

//        int color = comment.getColor();
//        int letter = comment.getLetter();
//        String content = comment.getContent();
//        boolean isOwner = comment.isOwnerComment();
//        final boolean isMyComment = comment.isMyComment();
//        int icon = (isOwner? mKing : mLetters[letter]);
//        Drawable mDrawable = mResources.getDrawable(icon);
//        if (!isOwner){
//            mDrawable.mutate().setColorFilter( mResources.getColor(mColors[color]), PorterDuff.Mode.MULTIPLY);
//        }
//        viewHolder.btn_icon.setImageDrawable(mDrawable);
        if (comment.isMyComment()){
            viewHolder.txt_name.setText("");
            viewHolder.txt_comment.setBackgroundResource(mRightBubble);
            viewHolder.layout_comment.setGravity(Gravity.RIGHT);
            viewHolder.layout_comment.addView(viewHolder.prg_loading);
            viewHolder.layout_comment.addView(viewHolder.txt_time);
            viewHolder.layout_comment.addView(viewHolder.txt_comment);
        } else {
            viewHolder.txt_name.setText(comment.getName());
            viewHolder.txt_comment.setBackgroundResource(mLeftBubble);
            viewHolder.layout_comment.setGravity(Gravity.LEFT);
            viewHolder.layout_comment.addView(viewHolder.txt_comment);
            viewHolder.layout_comment.addView(viewHolder.txt_time);
            viewHolder.layout_comment.addView(viewHolder.prg_loading);
        }

        viewHolder.txt_time.setText(comment.getTime());
        viewHolder.txt_comment.setText(comment.getContent());

        if (comment.getIsLoading()){
            viewHolder.prg_loading.setVisibility(View.VISIBLE);
        } else {
            viewHolder.prg_loading.setVisibility(View.GONE);
        }
        return convertView;
    }
}
