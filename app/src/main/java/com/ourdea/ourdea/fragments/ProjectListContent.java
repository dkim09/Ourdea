package com.ourdea.ourdea.fragments;

import com.ourdea.ourdea.dto.ProjectDto;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ProjectListContent {

    private List<ProjectDto> projectItems = new ArrayList<>();

    public List<ProjectDto> getProjectItems() {
        return projectItems;
    }

    public ProjectListContent(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                projectItems.add(new ProjectDto(jsonArray.getJSONObject(i)));
            } catch (Exception exception) { }
        }
    }

}
