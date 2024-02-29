package com.example.airpeek.ui.profile;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {


    private String name;
    private String email;
    private String country;
    private String image = null;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getImage() {
        return image;
    }

    public Profile(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.email = json.getString("email");
        this.country = json.getString("country");
        this.image = json.getString("image");
    }

}
