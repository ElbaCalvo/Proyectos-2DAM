package com.example.airpeek.ui.user_flights;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.JsonArrayRequestWithAuthentication;
import com.example.airpeek.JsonObjectRequestWithAuthentication;
import com.example.airpeek.R;
import com.example.airpeek.Server;

public class FragmentUserFlights extends Fragment {
    private RequestQueue queue;
    private Context context;
    private ConstraintLayout mainLayout;
    private ProgressBar progressBar;
    private UserFlightsList userFlights;
    private RecyclerView recyclerView;
    private ProgressBar pb1;

    public void setFlights(UserFlightsList userFlights) {
        this.userFlights = userFlights;
        UserFlightsAdapter myAdapter = new UserFlightsAdapter(this.userFlights);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_flights, container, false);
        pb1 = view.findViewById(R.id.loadingScreen);
        context = getContext();
        recyclerView = view.findViewById(R.id.user_flights_recycler_view);
        pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
        // Tras identificar el RecyclerView, pasamos a realizar la petición para obtener la info.
        // Si obtenemos respuesta, vamos leyendo todos los datos obtenidos de la url y
// pasandolos de arrays a objetos concretos y añadiendolos individualmente a
// una lista.
        JsonArrayRequestWithAuthentication request = new JsonArrayRequestWithAuthentication(
                Request.Method.GET,
                Server.name + "/user/flights",
                null,
                response -> {
                    pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                    setFlights(new UserFlightsList(response));
                },
                error -> {
                    pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                context);

        // Finalizada la configuración según el tipo de respuesta obtenido,
        // agregamos la solicitud a la cola de Volley para su procesamiento.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        return view;
    }

}