package com.ourdea.ourdea.fragments;

import com.ourdea.ourdea.dto.TaskDto;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TaskListContent {

    private List<TaskDto> taskItems = new ArrayList<>();

    public List<TaskDto> getTaskItems() {
        return taskItems;
    }

    public TaskListContent(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                taskItems.add(new TaskDto(jsonArray.getJSONObject(i)));
            } catch (Exception exception) { }
        }
    }

}
