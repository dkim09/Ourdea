package com.ourdea.ourdea.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ourdea.ourdea.api.models.TaskModel;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter<TaskModel> {

    public TaskListAdapter(Context context, int resource, List<TaskModel> objects) {
        super(context, resource, objects);
    }
}
