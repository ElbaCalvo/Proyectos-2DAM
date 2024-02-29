package com.example.airpeek.ui.specialoffers;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.JsonObjectRequestWithAuthentication;
import com.example.airpeek.R;
import com.example.airpeek.Server;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecialOffersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecialOffersFragment extends Fragment {

    private RequestQueue queue;
    private RecyclerView recyclerView;
    private SpecialOffersAdapter adapter;
    private List<SpecialOffersData> SpecialOffersDataList;

    // Códigos de los aeropuertos para asociarlos con sus nombres en arrays.xml
    private String[] airportCodes = {"DFW", "DXB", "IST", "LHR", "DEL", "BOM", "CDG", "JFK", "LAS", "AMS", "MIA",
            "MAD", "HND", "FRA", "MEX", "BCN", "CGK", "ATL", "HKG", "ICN", "TPE", "DOH", "BOG", "GRU", "SGN",
            "SIN", "JED", "MUC", "MNL", "CJU", "FCO", "SYD", "CAN", "CTU", "SZX", "CKG", "SHA", "PEK", "KMG",
            "PVG", "SVO", "CUN"};
    private String[] airportNames;
    private HashMap<String, String> airportMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar pb1;

    public SpecialOffersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpecialOffersFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecialOffersFragment newInstance(String param1, String param2) {
        SpecialOffersFragment fragment = new SpecialOffersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Se inicializan los códigos y nombres de aeropuertos
        airportNames = getResources().getStringArray(R.array.origin_and_destination);
        airportMap = new HashMap<>();

        for (int i = 0; i < airportCodes.length; i++) {
            airportMap.put(airportCodes[i], airportNames[i]);
        }
    }

    // Devuelve el nombre de un aeropuerto dado su código
    private String getAirportName(String code) {
        return airportMap.getOrDefault(code, code);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        pb1 = view.findViewById(R.id.loadingScreen);
        queue = Volley.newRequestQueue(getContext());

        SpecialOffersDataList = new ArrayList<>();
        adapter = new SpecialOffersAdapter(SpecialOffersDataList, (Activity) getContext());
        recyclerView = view.findViewById(R.id.recycler_view_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    // Realiza la solicitud de red para obtener datos y actualizar el adaptador.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
        // Para manejar las peticiones de red.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Se crea una solicitud JSON para obtener el array de ofertas especiales desde el servidor.
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET,
                        Server.name + "/special_offers",
                        null,
                        new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response) {
                                // Se crea una lista para almacenar los datos.
                                List<SpecialOffersData> allTheSavedPlaces = new ArrayList<>();

                                // Se analiza el JSON y se agrega cada oferta especial a la lista
                                for(int i=0; i<response.length(); i++) {
                                    try {
                                        JSONObject places = response.getJSONObject(i);
                                        String originCode = places.getString("departure_location");
                                        String destinationCode = places.getString("arrival_location");
                                        places.put("departure_location", getAirportName(originCode));
                                        places.put("arrival_location", getAirportName(destinationCode));
                                        SpecialOffersData data = new SpecialOffersData(places);
                                        SpecialOffersDataList.add(data);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }
}