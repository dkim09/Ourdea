package com.ourdea.ourdea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.MeetingDto;
import com.ourdea.ourdea.dto.ProjectDto;
import com.ourdea.ourdea.dto.TaskDto;

public class ProjectListAdapter extends ArrayAdapter<ProjectDto> {

    public ProjectListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.item_project, null);
        }

        ProjectDto project = super.getItem(position);

        TextView projectName = (TextView) view.findViewById(R.id.project_name);
        projectName.setText(project.getName());

        return view;
    }
}
