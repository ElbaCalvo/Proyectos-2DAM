package com.example.cornerfinder.recommended;

import org.json.JSONException;
import org.json.JSONObject;

// Clase que representa un elemento en el RecyclerView
public class RecyclerItems {

    // Variables para almacenar los datos de cada elemento
    private String place_name;
    private String description;
    private String tag;
    private String location;
    private String image_url;

    // Métodos getter para cada variable
    public String getPlace_Name() { return place_name; }
    public String getDescription() { return description; }
    public String getTag() { return tag; }
    public String getLocation() { return location; }
    public String getImage_url() { return image_url; }

    // Constructor que toma los datos como parámetros
    public RecyclerItems(String place_name, String description, String location, String tag, String image_url){
        this.place_name=place_name;
        this.description=description;
        this.tag=tag;
        this.location=location;
        this.image_url=image_url;
    }

    // Constructor que toma un objeto JSON y extrae los datos
    public RecyclerItems(JSONObject json){
        try{
            this.place_name = json.getString("place_name");
            this.description = json.getString("description");
            this.tag = json.getString("tag");
            this.location = json.getString("location");
            this.image_url = json.getString("image_url");
        }catch (JSONException e){ e.printStackTrace(); }
    }
}