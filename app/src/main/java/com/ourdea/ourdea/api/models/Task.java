package com.ourdea.ourdea.api.models;

import org.json.JSONObject;

public class Task {
    private String label;
    private String description;
    private String assignedTo;
    private Object dueDate;
    private String id;
    private String name;

    public Task(String id, String name, String description, String assignedTo, String label, String dueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assignedTo = assignedTo;
        this.label = label;
        this.dueDate = dueDate;
    }

    public Task(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.assignedTo = jsonObject.getString("assignedTo");
            this.label = jsonObject.getString("label");
            this.dueDate = jsonObject.get("dueDate");
        } catch (Exception exception) {

        }
    }

    @Override
    public String toString() {
        return name;
    }
}