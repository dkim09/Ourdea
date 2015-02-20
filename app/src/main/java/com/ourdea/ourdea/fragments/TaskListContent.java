package com.ourdea.ourdea.fragments;

import com.ourdea.ourdea.api.models.TaskModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TaskListContent {

    private List<TaskModel> taskItems = new ArrayList<>();

    public List<TaskModel> getTaskItems() {
        return taskItems;
    }

    public TaskListContent(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                taskItems.add(new TaskModel(jsonArray.getJSONObject(i)));
            } catch (Exception exception) { }
        }
    }

}
