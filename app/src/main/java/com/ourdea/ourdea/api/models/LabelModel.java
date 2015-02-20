package com.ourdea.ourdea.api.models;

import org.json.JSONObject;

public class LabelModel {

    private String name;

    public String getName() {
        return name;
    }

    public LabelModel(String name) {
        this.name = name;
    }

    public LabelModel(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
        } catch (Exception exception) { }
    }

}
