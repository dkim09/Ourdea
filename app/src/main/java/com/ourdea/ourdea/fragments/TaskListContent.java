package com.ourdea.ourdea.fragments;

import com.ourdea.ourdea.api.models.Task;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TaskListContent {

    private List<Task> taskItems = new ArrayList<>();

    public List<Task> getTaskItems() {
        return taskItems;
    }

    public TaskListContent(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                taskItems.add(new Task(jsonArray.getJSONObject(i)));
            } catch (Exception exception) { }
        }
    }

}
