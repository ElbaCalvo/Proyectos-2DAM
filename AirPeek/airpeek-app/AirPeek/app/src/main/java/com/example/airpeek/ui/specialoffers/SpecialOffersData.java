package com.example.airpeek.ui.specialoffers;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpecialOffersData {
    private String id;
    private String origin;
    private String destination;
    private String origin_hour;
    private String arrival_hour;
    private String origin_date;
    private String arrival_date;
    private String price;
    private String buy_url;

    public SpecialOffersData(JSONObject jsonObject) throws JSONException {
        // Se extraen los datos del JSONObject y se asignan a las variables
        this.id = jsonObject.getString("id");
        this.origin = jsonObject.getString("departure_location");
        this.destination = jsonObject.getString("arrival_location");
        this.price = jsonObject.getString("price");
        this.buy_url = jsonObject.getString("buyUrl");

        try {
            // Parseo de fechas a objetos Date
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            Date departureDatetime = parser.parse(jsonObject.getString("departure_datetime"));
            this.origin_date = new SimpleDateFormat("dd/MM/yyyy").format(departureDatetime);
            this.origin_hour = new SimpleDateFormat("HH:mm").format(departureDatetime);

            Date arrivalDatetime = parser.parse(jsonObject.getString("arrival_datetime"));
            this.arrival_date = new SimpleDateFormat("dd/MM/yyyy").format(arrivalDatetime);
            this.arrival_hour = new SimpleDateFormat("HH:mm").format(arrivalDatetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Obtención de los valores de las variables
    public String getId(){
        return id;
    }
    public String getOrigin() {
        return origin;
    }
    public String getDestination() {
        return destination;
    }
    public String getOriginHour() {
        return origin_hour;
    }
    public String getArrivalHour(){
        return arrival_hour;
    }
    public String getOriginDate() { return origin_date; }
    public String getArrivalDate() { return arrival_date; }
    public String getPrice() { return price + "€"; }
    public String getBuyUrl() { return buy_url; }
}
