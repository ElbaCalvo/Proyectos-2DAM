package com.example.cornerfinder.ui.account;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountData {

    private String placeName;
    private String descripcion;
    private String image_url;

    public String getPlaceName() { return placeName; }

    public String getDescripcion() { return descripcion; }

    public String getImage_url() { return image_url; }

    public AccountData(String placeName, String descripcion,String image_url){
        this.placeName=placeName;
        this.descripcion=descripcion;
        this.image_url=image_url;
    }

    public AccountData(JSONObject json){
        try{
            this.placeName = json.getString("place_name");
            this.descripcion = json.getString("description");
            this.image_url = json.getString("image_url");
        }catch (JSONException e){ e.printStackTrace(); }
    }
}
