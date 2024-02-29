package com.example.cornerfinder.savedplaces;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cornerfinder.R;
import com.example.cornerfinder.savedplaces.SavedPlacesAdapter;
import com.example.cornerfinder.recommended.RecyclerItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedPlacesFragment extends Fragment {
    private RecyclerView recyclerView;
    private SavedPlacesAdapter adapter;
    private List<RecyclerItems> savedPlacesDataList;
    private RequestQueue queue;

    private RequestQueue requestQueue;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedPlacesFragment() {
        // Required empty public constructor
    }

    public static SavedPlacesFragment newInstance(String param1, String param2) {
        SavedPlacesFragment fragment = new SavedPlacesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Método llamado al crear el fragmento
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Método llamado al crear la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        queue = Volley.newRequestQueue(getContext());

        // Inicializar la lista de datos de lugares guardados
        savedPlacesDataList = new ArrayList<>();

        // Crear un adaptador para el RecyclerView.
        adapter = new SavedPlacesAdapter(savedPlacesDataList, this);
        recyclerView = view.findViewById(R.id.recycler_view_item);
        recyclerView.setAdapter(adapter);
        // Establecer un diseño de LinearLayout para el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // Crear una solicitud JSON de matriz para obtener los lugares guardados
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET,
                        "https://raw.githubusercontent.com/Bl4nc018/Proyectos-2-trimestre/main/saved_places.json",
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Crear una lista para almacenar los datos de lugares guardados
                                List<RecyclerItems> allTheSavedPlaces = new ArrayList<>();
                                for(int i=0; i<response.length(); i++) {
                                    try {
                                        // Obtener un objeto JSON que representa un lugar guardado
                                        JSONObject places = response.getJSONObject(i);
                                        // Crear un objeto RecyclerItems a partir del objeto JSON
                                        RecyclerItems data = new RecyclerItems(places);
                                        // Agregar el objeto RecyclerItems a la lista de datos
                                        savedPlacesDataList.add(data);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //Notificar al adaptador que se han añadido nuevos datos
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }
}
