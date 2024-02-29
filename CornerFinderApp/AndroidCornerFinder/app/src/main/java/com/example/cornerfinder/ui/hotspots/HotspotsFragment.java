package com.example.cornerfinder.ui.hotspots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cornerfinder.R;
import com.example.cornerfinder.recommended.RecyclerAdapter;
import com.example.cornerfinder.recommended.RecyclerItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotspotsFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<RecyclerItems> allTheHotspots;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_item);
        allTheHotspots = new ArrayList<>();

        // Tras identificar el RecyclerView, pasamos a realizar la petición para obtener la info.
        // Si obtenemos respuesta, vamos leyendo todos los datos obtenidos de la url y
// pasandolos de arrays a objetos concretos y añadiendolos individualmente a
// una lista.
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "https://raw.githubusercontent.com/Bl4nc018/Proyectos-2-trimestre/main/saved_places.json",
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject game = response.getJSONObject(i);
                            RecyclerItems data = new RecyclerItems(game);
                            allTheHotspots.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Creamos un adaptador con la lista de datos y la actividad asociada.
                    RecyclerAdapter adapter = new RecyclerAdapter(allTheHotspots, this);

                    // Configuramos el RecyclerView con el adaptador y un LinearLayoutManager.
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                },
                error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        // Finalizada la configuración según el tipo de respuesta obtenido,
        // agregamos la solicitud a la cola de Volley para su procesamiento.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        return view;
    }

}
