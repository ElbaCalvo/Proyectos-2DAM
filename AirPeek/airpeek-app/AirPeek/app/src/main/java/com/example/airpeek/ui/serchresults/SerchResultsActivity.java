package com.example.airpeek.ui.serchresults;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.airpeek.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SerchResultsActivity extends AppCompatActivity {

    // Códigos de los aeropuertos para asociarlos con sus nombres en arrays.xml
    private String[] airportCodes = {"DFW", "DXB", "IST", "LHR", "DEL", "BOM", "CDG", "JFK", "LAS", "AMS", "MIA",
            "MAD", "HND", "FRA", "MEX", "BCN", "CGK", "ATL", "HKG", "ICN", "TPE", "DOH", "BOG", "GRU", "SGN",
            "SIN", "JED", "MUC", "MNL", "CJU", "FCO", "SYD", "CAN", "CTU", "SZX", "CKG", "SHA", "PEK", "KMG",
            "PVG", "SVO", "CUN"};

    private String[] airportNames;

    private HashMap<String, String> airportMap;
    private int adults, kids, babies;

    private double weight;

    // Devuelve el nombre de un aeropuerto dado su código
    private String getAirportName(String code) {
        return airportMap.getOrDefault(code, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Se establece el contenido de la actividad a partir del XML
        setContentView(R.layout.recycler_view);

        // Se obtiene de la referencia al RecyclerView del XML
        RecyclerView recyclerView = findViewById(R.id.recycler_view_list);

        // Se gestionar la disposición de elementos en el RecyclerView.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Se obtiene la cadena JSON del Intent
        String jsonFlightInfo = getIntent().getStringExtra("jsonFlightInfo");

        // Se obtienen los datos de los pasajeros
        this.adults = getIntent().getIntExtra("adults", 0);
        this.kids = getIntent().getIntExtra("kids", 0);
        this.babies = getIntent().getIntExtra("babies", 0);
        this.weight =  adults+kids*0.5+babies*0.1;

        // Se crea de una lista vacía para almacenar los datos de búsqueda
        ArrayList<SerchResultsData> searchDataList = new ArrayList<>();

        // Se inicializan los códigos y nombres de aeropuertos
        airportNames = getResources().getStringArray(R.array.origin_and_destination);
        airportMap = new HashMap<>();

        for (int i = 0; i < airportCodes.length; i++) {
            airportMap.put(airportCodes[i], airportNames[i]);
        }

        if (jsonFlightInfo != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonFlightInfo);  // Se convierte la cadena JSON en un JSONObject

                JSONArray cheapestArray = jsonObject.getJSONArray("cheapest"); // Obtiene el array "cheapest" del JSONObject
                // Para cada grupo de tres vuelos en el array
                for (int i = 0; i < cheapestArray.length(); i+=3) {
                    // Obtiene los tres objetos de vuelo
                    if (i + 2 < cheapestArray.length()) { // Se comprueba si hay al menos tres elementos restantes en el array
                        JSONObject flightObject1 = cheapestArray.getJSONObject(i);
                        JSONObject flightObject2 = cheapestArray.getJSONObject(i + 1);
                        JSONObject flightObject3 = cheapestArray.getJSONObject(i + 2);

                        // Se reemplazan los códigos de los aeropuertos por los nombres
                        String originCode1 = flightObject1.getString("departure_location");
                        String destinationCode1 = flightObject1.getString("arrival_location");
                        flightObject1.put("departure_location", getAirportName(originCode1));
                        flightObject1.put("arrival_location", getAirportName(destinationCode1));

                        String originCode2 = flightObject2.getString("departure_location");
                        String destinationCode2 = flightObject2.getString("arrival_location");
                        flightObject2.put("departure_location", getAirportName(originCode2));
                        flightObject2.put("arrival_location", getAirportName(destinationCode2));

                        String originCode3 = flightObject3.getString("departure_location");
                        String destinationCode3 = flightObject3.getString("arrival_location");
                        flightObject3.put("departure_location", getAirportName(originCode3));
                        flightObject3.put("arrival_location", getAirportName(destinationCode3));

                        // Crea un nuevo objeto SerchResultsData a partir de los objetos de vuelo
                        SerchResultsData flightData = new SerchResultsData(flightObject1, flightObject2, flightObject3, weight);
                        searchDataList.add(flightData); // Añade el objeto a la lista
                    }
                }

                // Para los vuelos más rápidos
                JSONArray fastestArray = jsonObject.getJSONArray("fastest"); // Obtiene el array "fastest" del JSONObject
                // Para cada grupo de tres vuelos en el array
                for (int i = 0; i < fastestArray.length(); i+=3) {
                    // Obtiene los tres objetos de vuelo
                    if (i + 2 < fastestArray.length()) { // Se comprueba si hay al menos tres elementos restantes en el array
                        JSONObject flightObject1 = fastestArray.getJSONObject(i);
                        JSONObject flightObject2 = fastestArray.getJSONObject(i + 1);
                        JSONObject flightObject3 = fastestArray.getJSONObject(i + 2);

                        // Se reemplazan los códigos de los aeropuertos por los nombres
                        String originCode1 = flightObject1.getString("departure_location");
                        String destinationCode1 = flightObject1.getString("arrival_location");
                        flightObject1.put("departure_location", getAirportName(originCode1));
                        flightObject1.put("arrival_location", getAirportName(destinationCode1));

                        String originCode2 = flightObject2.getString("departure_location");
                        String destinationCode2 = flightObject2.getString("arrival_location");
                        flightObject2.put("departure_location", getAirportName(originCode2));
                        flightObject2.put("arrival_location", getAirportName(destinationCode2));

                        String originCode3 = flightObject3.getString("departure_location");
                        String destinationCode3 = flightObject3.getString("arrival_location");
                        flightObject3.put("departure_location", getAirportName(originCode3));
                        flightObject3.put("arrival_location", getAirportName(destinationCode3));

                        // Crea un nuevo objeto SerchResultsData a partir de los objetos de vuelo
                        SerchResultsData flightData = new SerchResultsData(flightObject1, flightObject2, flightObject3, weight);
                        searchDataList.add(flightData); // Añade el objeto a la lista
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Se crea e inicialización del adaptador
        SerchResultsAdapter searchResultsAdapter = new SerchResultsAdapter(searchDataList, this);
        // Se establece del adaptador en el RecyclerView
        recyclerView.setAdapter(searchResultsAdapter);
    }

}
