package com.example.airpeek.ui.notifications;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationsItem {
    private String flightId;
    private LocalDateTime departure_datetime;
    private LocalDateTime arrival_datetime;


    public LocalDateTime getDepartureDateTime() {
        return departure_datetime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrival_datetime;
    }
    public String getflightId() {
        return flightId;
    }

    public NotificationsItem(JSONObject json) throws JSONException{
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        this.flightId = json.getString("flight_id");
        this.departure_datetime = LocalDateTime.parse(json.getString("departure_datetime"), formatter);
        this.arrival_datetime = LocalDateTime.parse(json.getString("arrival_datetime"), formatter);
    }
}