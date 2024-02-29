package com.example.airpeek.ui.user_flights;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFlightsList {
    private List<UserFlight> userFlights;

    // Almacena los vuelos en una lista

    public UserFlightsList(JSONArray array) {
        userFlights = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject jsonElement = array.getJSONObject(i);
                UserFlight aUserFlight = new UserFlight(jsonElement);
                userFlights.add(aUserFlight);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<UserFlight> getUserFlights() {
        return userFlights;
    }
}
