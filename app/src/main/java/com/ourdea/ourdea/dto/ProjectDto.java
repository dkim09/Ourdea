package com.ourdea.ourdea.dto;

public class ProjectDto {

    private Long projectId;

    private String name;

    private String password;

    public ProjectDto(String name, String password) {
        this.name = name;
        this.password = password;
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
