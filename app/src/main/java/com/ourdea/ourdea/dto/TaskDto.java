package com.ourdea.ourdea.dto;

import org.json.JSONObject;

public class TaskDto {
    private String label;
    private String description;
    private String assignedTo;
    private Long dueDate;
    private String id;
    private String name;
    private String status;

    public TaskDto(String name, String description, String assignedTo, String label, Long dueDate, String status) {
        this.name = name;
        this.description = description;
        this.assignedTo = assignedTo;
        this.label = label;
        this.dueDate = dueDate;
        this.status = status;
    }

    public TaskDto(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.assignedTo = jsonObject.getJSONObject("assignedTo").getString("email");
            this.label = jsonObject.getJSONObject("label").getString("name");
            this.dueDate = jsonObject.getLong("dueDate");
            this.status = jsonObject.getString("status");
        } catch (Exception exception) {

        }
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

}
