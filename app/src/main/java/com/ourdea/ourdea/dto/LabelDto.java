package com.ourdea.ourdea.dto;

import org.json.JSONObject;

public class LabelDto {

    private String name;

    public String getName() {
        return name;
    }

    public LabelDto(String name) {
        this.name = name;
    }

    public LabelDto(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
        } catch (Exception exception) { }
    }

}
