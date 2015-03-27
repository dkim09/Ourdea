package com.ourdea.ourdea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ourdea.ourdea.dto.MeetingDto;

import java.text.SimpleDateFormat;

public class MeetingListAdapter extends ArrayAdapter<MeetingDto> {

    public MeetingListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        MeetingDto meeting = super.getItem(position);

        TextView textOne = (TextView) view.findViewById(android.R.id.text1);
        textOne.setText(meeting.getName());

        TextView textTwo = (TextView) view.findViewById(android.R.id.text2);
        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy h:mm a");
        String formattedDateString = formatter.format(meeting.getTime());
        textTwo.setText(formattedDateString);

        return view;
    }

}
