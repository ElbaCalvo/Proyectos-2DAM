package com.example.cornerfinder.routes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cornerfinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Routes extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private RequestQueue requestQueue;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationClient;
    public Routes(){}  // Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Al crear la vista, inflamos el layout correspondiente para que se vea el fragment.
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        // Instanciamos el request queue.
        requestQueue = Volley.newRequestQueue(getContext());

        // Obtenemos la localización del cliente.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Instanciamos el mapa y llamamos a su layout.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) { mapFragment.getMapAsync(this); }

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Crear un BitmapDescriptor morado
        BitmapDescriptor purpleIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
        BitmapDescriptor orangeIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        // Añadir marcadores para las playas y los lugares de interés
        LatLng asLapas = new LatLng(43.383636, -8.405864);
        mMap.addMarker(new MarkerOptions().position(asLapas).title("Playa das Lapas").icon(purpleIcon));

        LatLng matadero = new LatLng(43.375622, -8.403764);
        mMap.addMarker(new MarkerOptions().position(matadero).title("Playa de Matadero").icon(purpleIcon));

        LatLng orzan = new LatLng(43.3699, -8.40618);
        mMap.addMarker(new MarkerOptions().position(orzan).title("Playa de Orzán").icon(purpleIcon));

        LatLng riazor = new LatLng(43.3686, -8.4113);
        mMap.addMarker(new MarkerOptions().position(riazor).title("Playa de Riazor").icon(purpleIcon));

        LatLng escondite = new LatLng(43.368200, -8.395100);
        mMap.addMarker(new MarkerOptions().position(escondite).title("Café El Escondite").icon(orangeIcon));

        LatLng cervantes = new LatLng(43.371800, -8.395000);
        mMap.addMarker(new MarkerOptions().position(cervantes).title("Librería Cervantes").icon(orangeIcon));

        LatLng encrucijada = new LatLng(43.370500, -8.395800);
        mMap.addMarker(new MarkerOptions().position(encrucijada).title("La Encrucijada").icon(orangeIcon));

        // Ajustar el zoom para incluir todos los marcadores
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(asLapas);
        builder.include(matadero);
        builder.include(orzan);
        builder.include(riazor);
        builder.include(escondite);
        builder.include(cervantes);
        builder.include(encrucijada);
        LatLngBounds bounds = builder.build();

        int padding = 200; // Ajuste del espacio alrededor de los marcadores
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

        // Comprobamos que los permisos de ubicación están concedidos:
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }

        mMap.setMyLocationEnabled(true);  // Habilitamos la capa de "Mi ubicación" en el mapa.


        // Obtenemos la última ubicación conocida del dispositivo:
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(),
                location -> {
                    if (location != null) {
                        lastKnownLocation = location;
                        Toast.makeText(getActivity(), location.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Configuramos el listener para el clic en los marcadores.
        mMap.setOnMarkerClickListener(marker -> {
            LatLng destination = marker.getPosition();
            calculateRoute(lastKnownLocation, destination);
                return true;
        });
    }


    // Obtenemos la URL de la API para obtener las direcciones:
    private String getDirectionsUrl(LatLng origin, LatLng destination, String apiKey){
        String str_origin = "origin="+origin.latitude+","+origin.longitude;;
        String str_dest = "destination="+destination.latitude+","+destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";

        String url = "https://maps.googleapis.com/maps/api/directions/json?"+
                str_origin+"&"+str_dest+"&"+sensor+"&"+mode+"&key="+apiKey;

        return url;
    }


    private void drawRoute(JSONObject response){
        try{
            Toast.makeText(getContext(), "Dibujando ruta ...", Toast.LENGTH_SHORT).show();

            // Obtenemos la ruta principal.
            JSONObject route = response.getJSONArray("routes").getJSONObject(0);
            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
            String encodedPath = overviewPolyline.getString("points");
            List<LatLng> path = PolyUtil.decode(encodedPath);

            // Dibuja la ruta en el mapa
            mMap.addPolyline(new PolylineOptions().addAll(path));
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateRoute(Location origin, LatLng destination){
        if(origin==null){
            Toast.makeText(getContext(), "No hay origen", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng originLatLng = new LatLng(origin.getLatitude(), origin.getLongitude());
        String apiKey = "AIzaSyDepnk_Vlv1_9OT6MM-wPQ9LsqzQ1nKZtI";
        String url = getDirectionsUrl(originLatLng, destination, apiKey);


        // Crear una solicitud JSON para obtener la ruta desde la API de Google Maps.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> drawRoute(response), error -> error.printStackTrace());


        // Añadimos la solicitud a la cola de las mismas.
        requestQueue.add(request);
    }

}
