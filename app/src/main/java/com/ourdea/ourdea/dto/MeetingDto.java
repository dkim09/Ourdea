package com.ourdea.ourdea.dto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MeetingDto {

    String name;

    String description;

    String location;

    Long time;

    ArrayList<String> agreements;

    Boolean active;

    Long project;

    public MeetingDto(JSONObject json) {
        try {
            this.name = json.getString("name");
            this.description = json.getString("description");
            this.location = json.getString("location");
            this.time = json.getLong("time");
            this.active = json.getBoolean("active");
            this.agreements = new ArrayList<>();
            this.project = json.getJSONObject("project").getLong("projectId");

            JSONArray agreementsJSON = json.getJSONArray("agreements");
            for (int a = 0; a < agreementsJSON.length(); a++) {
                JSONObject agreement = agreementsJSON.getJSONObject(0);

                this.agreements.add(agreement.getString("name") + "\n" + agreement.getString("email"));
            }
        } catch (Exception exception) {
            Log.d("JSON_ERROR", "Error parsing MeetingJSON into MeetingDto");
        }
    }

    public MeetingDto(String name, String description, Long time, String location, Long project) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.location = location;
        this.project = project;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public ArrayList<String> getAgreements() {
        return agreements;
    }

    public void setAgreements(ArrayList<String> agreements) {
        this.agreements = agreements;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
