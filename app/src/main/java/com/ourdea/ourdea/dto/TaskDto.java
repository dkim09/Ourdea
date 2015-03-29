package com.ourdea.ourdea.dto;

import org.json.JSONObject;

import java.util.Calendar;

public class TaskDto {
    private String label;
    private String description;
    private String assignedTo;
    private Long dueDate;
    private String id;
    private String name;
    private String status;

    public TaskDto() {

    }

    public TaskDto(String name, String description, String assignedTo, String label, Calendar dueDateTime, String status) {
        this.name = name;
        this.description = description;
        this.assignedTo = assignedTo;
        this.label = label;
        if (dueDateTime != null) {
            this.dueDate = dueDateTime.getTimeInMillis();
        } else {
            this.dueDate = null;
        }
        this.status = status;
    }

    public TaskDto(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.assignedTo = jsonObject.getJSONObject("assignedTo").getString("email");
            this.label = jsonObject.getJSONObject("label").getString("name");
            if (!jsonObject.isNull("dueDate")) {
                this.dueDate = jsonObject.getLong("dueDate");
            }
            this.status = jsonObject.getString("status");
        } catch (Exception exception) {

        }
    }

    @Override
    public String toString() {
        return name.equals("") ? "Task" : name;
    }

    public String getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description.equals("") ? "Description" : description;
    }

    public String getAssignedTo() {
        return assignedTo.equals("") ? "me" : assignedTo;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
