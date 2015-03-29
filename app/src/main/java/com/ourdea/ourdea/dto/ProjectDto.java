package com.ourdea.ourdea.dto;

import org.json.JSONObject;

public class ProjectDto {

    private Long projectId;

    private String name;

    private String password;

    public ProjectDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public ProjectDto(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.projectId = jsonObject.getLong("projectId");
            this.password = jsonObject.getString("password");
        } catch (Exception exception) {

        }
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
