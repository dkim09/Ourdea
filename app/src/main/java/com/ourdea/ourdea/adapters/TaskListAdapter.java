package com.ourdea.ourdea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.TaskDto;

public class TaskListAdapter extends ArrayAdapter<TaskDto> {

    public TaskListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.item_task, null);
        }

        TaskDto task = super.getItem(position);

        TextView taskName = (TextView) view.findViewById(R.id.task_name);
        taskName.setText(task.getName());

        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);
        taskDescription.setText(task.getDescription());

        if (!task.getLabel().equals("")) {
            TextView taskLabel = (TextView) view.findViewById(R.id.task_label);
            taskLabel.setText("#" + task.getLabel());
        }

        return view;
    }
}
