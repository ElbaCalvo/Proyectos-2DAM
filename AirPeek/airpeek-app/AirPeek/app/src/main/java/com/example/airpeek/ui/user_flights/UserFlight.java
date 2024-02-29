package com.example.airpeek.ui.user_flights;

import org.json.JSONException;
import org.json.JSONObject;

public class UserFlight {
    private String flightId;
    private String departureTime;
    private String arrivalTime;
    private String departureDate;
    private String arrivalDate;
    private String origin;
    private String destination;

    public String getFlightId(){
        return flightId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
// %Y-%m-%d %H:%M:%S
    public UserFlight(JSONObject json) throws JSONException {
        this.flightId = json.getString("flight_id");
        this.departureTime = json.getString("departure_datetime").split("T")[1];
        this.arrivalTime = json.getString("arrival_datetime").split("T")[1];
        this.departureDate = json.getString("departure_datetime").split("T")[0];
        this.arrivalDate = json.getString("arrival_datetime").split("T")[0];
        this.origin = json.getString("departure_location");
        this.destination = json.getString("arrival_location");
    }
}
