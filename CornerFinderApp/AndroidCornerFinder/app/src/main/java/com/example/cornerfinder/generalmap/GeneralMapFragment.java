package com.example.cornerfinder.generalmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cornerfinder.MainActivity;
import com.example.cornerfinder.R;
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

public class GeneralMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;


    // Esta variable es para variar la visibilidad del botón de añadir un sitio al mapa general.
    private boolean isButton2Visible = false;

    public GeneralMapFragment(){} // Constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generalmap, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) { mapFragment.getMapAsync(this); }


        // Definimos los dos botones para el añadido de marcadores a nuestro mapa general.
        Button addButton = view.findViewById(R.id.addButton);
        Button addButton2 = view.findViewById(R.id.addButton2);

        // Jugamos con un listener para hacer el 2º botón invisible según si se hace click en el
        // primero o no.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton2Visible) {
                    addButton2.setVisibility(View.INVISIBLE);
                } else {
                    addButton2.setVisibility(View.VISIBLE);
                }
                isButton2Visible = !isButton2Visible;
            }
        });

        // Si pulsamos el botón "Añadir...", nos llevará a otra pantalla donde se llevará a cabo
        // ese proceso de forma más completa.
        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para ir al mainActivity e inflar alli el fragmento.
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.setAction("ACTION_START_ADD_LOCATION");
                startActivity(intent);
            }
        });
        return view;
    }


    // Aquí ya trabajamos con el mapa para trabajar con los diversos sitios ya registrados en la app.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        // Creamos un Bitmap para poder cambiar el color de los marcadores.
        BitmapDescriptor purpleIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
        BitmapDescriptor orangeIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);


        // Añadimos los marcadores deseados:
        LatLng yo = new LatLng(43.36700, -8.412600);
        mMap.addMarker(new MarkerOptions().position(yo).title("Mi ubicación"));

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

        LatLng sanPedro = new LatLng(43.368900, -8.402600);
        mMap.addMarker(new MarkerOptions().position(sanPedro).title("Parque de San Pedro").icon(orangeIcon));

        LatLng galArte = new LatLng(43.369736,-8.399628);
        mMap.addMarker(new MarkerOptions().position(galArte).title("Galería de arte").icon(orangeIcon));


        // Para ajustar la cámara del mapa, jugamos con LatLngBounds. Con el builder añadiremos los
        // marcadores deseados al mapa y la clase realizará el ajuste deseado para que aparezcan
        // todos los marcadores en el mapa.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(yo);
        builder.include(asLapas);
        builder.include(matadero);
        builder.include(orzan);
        builder.include(riazor);
        builder.include(escondite);
        builder.include(cervantes);
        builder.include(encrucijada);
        builder.include(sanPedro);
        builder.include(galArte);
        LatLngBounds bounds = builder.build();

        int padding = 200; // Puedes ajustar el espacio de los bordes.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);


        // A partir de aquí, la clase va a trabajar con los datos de ubicaciones específicas.
        // Llamamos al bundle con la ubicación y la almacenamos en una variable.
        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey("location")) {
            String location = bundle.getString("location");

            if (location != null) {
                // Parsear las coordenadas (supongamos que están separadas por coma)
                String[] coordinates = location.split(",");
                if (coordinates.length == 2) {
                    double latitude = Double.parseDouble(coordinates[0]);
                    double longitude = Double.parseDouble(coordinates[1]);

                    // Enfocar la cámara del mapa en las coordenadas especificadas
                    LatLng targetLocation = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 18f));
                }
            }
        }
    }
}
