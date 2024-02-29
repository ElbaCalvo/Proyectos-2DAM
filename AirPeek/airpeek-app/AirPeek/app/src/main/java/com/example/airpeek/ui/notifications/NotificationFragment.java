package com.example.airpeek.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.JsonArrayRequestWithAuthentication;
import com.example.airpeek.R;
import com.example.airpeek.Server;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private List<NotificationsItem> notificationsList;
    private ProgressBar pb1;

    private RequestQueue requestQueue;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    // Llamado cuando el fragmento es creado
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    // Llamado cuando la vista para el fragmento está siendo creada
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Configuración del badge (Icono de notificación) en el BottomNavigationView
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        pb1 = view.findViewById(R.id.loadingScreen);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
        badge.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
        badge.setBadgeTextColor(ContextCompat.getColor(getContext(), R.color.white));
        badge.setMaxCharacterCount(15);

        // Inicialización de la lista, el adaptador y el RecyclerView
        notificationsList = new ArrayList<>();
        adapter = new NotificationsAdapter(badge, notificationsList, (Activity) getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    // Llamado cuando la vista del fragmento ha sido creada
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
        Context context = getContext();
        // Inicialización de la RequestQueue
        this.requestQueue = Volley.newRequestQueue(getContext());
        // Creación de la solicitud JsonArrayRequest
        JsonArrayRequestWithAuthentication request = new JsonArrayRequestWithAuthentication
                (Request.Method.GET,
                        Server.name + "/user/flights",
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                                // En respuesta a la solicitud, se crea una lista de vuelos
                                List<NotificationsItem> flights = new ArrayList<>();
                                for(int i=0; i<response.length(); i++) {
                                    try {
                                        JSONObject flight = response.getJSONObject(i);
                                        NotificationsItem item = new NotificationsItem(flight);
                                        notificationsList.add(item);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.setNotificationsList(notificationsList);
                                adapter.notifyDataSetChanged();


                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        // Manejo de errores
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        error.printStackTrace();
                    }
                },context);
        // Añadir la solicitud a la RequestQueue
        this.requestQueue.add(request);
    }
}