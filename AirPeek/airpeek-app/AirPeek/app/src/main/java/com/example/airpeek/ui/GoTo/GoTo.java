package com.example.airpeek.ui.GoTo;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.MainActivity;
import com.example.airpeek.R;
import com.example.airpeek.Server;
import com.example.airpeek.ui.RoundTrip.RoundTrip;

import com.example.airpeek.ui.serchresults.SerchResultsActivity;

import org.json.JSONObject;

import java.util.Calendar;

public class GoTo extends Fragment {
    private GoToData goToData = new GoToData(); // Esta es la clase para trabajar con los datos.

    // Estas variables son para trabajar con los aeropuertos:
    private String originAirport;
    private String destinationAirport;
    private String[] airports = {"DFW", "DXB", "IST", "LHR", "DEL", "BOM", "CDG", "JFK", "LAS",
            "AMS", "MIA", "MAD", "HND", "FRA", "MEX", "BCN", "CGK", "ATL", "HKG", "ICN", "TPE",
            "DOH", "BOG", "GRU", "SGN", "SIN", "JED", "MUC", "MNL", "CJU", "FCO", "SYD", "CAN",
            "CTU", "SZX", "CKG", "SHA", "PEK", "KMG", "PVG", "SVO", "CUN"};


    // Estas variables son para el tipo y la cantidad de billetes:
    int counter = 0;
    int counter2 = 0;
    int counter3 = 0;
    private TextView adults, kids, babies;
    private Button buttonA1, buttonA2, buttonK1, buttonK2, buttonB1, buttonB2;
    private ProgressBar pb1;
    private EditText departureDate; // Aquí trabajamos con la fecha del vuelo de ida.


    public GoTo() { }  // Constructor.


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_go_to, container, false);

        pb1 = view.findViewById(R.id.loadingScreen);


        // Esto es para el origen y destino de los vuelos:
        Spinner origin = view.findViewById(R.id.spinner_origin); // Definimos los spinners (menús desplegables).
        Spinner destination = view.findViewById(R.id.spinner_destination);


        // ArrayAdapter es lo que ayuda a representar en la pantalla cada elemento de la
        // matriz al acceder al spinner. Se apoya en el array de origenes y destinos para
        // crear las opciones deseadas.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.origin_and_destination, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        origin.setAdapter(adapter);
        destination.setAdapter(adapter);


        // Una vez es seleccionado un item, se guarda la posición de este, que será empleada en un
        // array con todos los nombres de aeropuertos hardcodeados para guardar las correspondientes
        // siglas del aeropuerto.
        origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                originAirport = airports[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {  }
        });

        destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                destinationAirport = airports[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });


        // Botón para intercambiar el destino por el vuelo y viceversa:
        Button changer = view.findViewById(R.id.change_origin_and_destination);
        changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el origen y destino actuales y los almacenamos en variables.
                int indexOrigin = origin.getSelectedItemPosition();
                int indexDestination = destination.getSelectedItemPosition();

                // Intercambiamos los valores entre sí.
                destination.setSelection(indexOrigin);
                origin.setSelection(indexDestination);
            }
        });


        // A continuación pasaremos con el tipo y cantidad de billetes.
        // Billetes para adultos:

        adults = view.findViewById(R.id.adult_ticket);
        buttonA1 = view.findViewById(R.id.buttonA1);
        buttonA2 = view.findViewById(R.id.buttonA2);

        buttonA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter = counter - 1;
                    adults.setText(String.valueOf(counter));
                }
            }
        });
        buttonA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                adults.setText(String.valueOf(counter));
            }
        });


        // Billetes para niños:

        kids = view.findViewById(R.id.kid_ticket);
        buttonK1 = view.findViewById(R.id.buttonK1);
        buttonK2 = view.findViewById(R.id.buttonK2);

        buttonK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter2 > 0) {
                    counter2 = counter2 - 1;
                    kids.setText(String.valueOf(counter2));
                }
            }
        });
        buttonK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter2 = counter2 + 1;
                kids.setText(String.valueOf(counter2));
            }
        });


        // Billetes para bebes:

        babies = view.findViewById(R.id.baby_ticket);
        buttonB1 = view.findViewById(R.id.buttonB1);
        buttonB2 = view.findViewById(R.id.buttonB2);

        buttonB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter3 > 0) {
                    counter3 = counter3 - 1;
                    babies.setText(String.valueOf(counter3));
                }
            }
        });
        buttonB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter3 = counter3 + 1;
                babies.setText(String.valueOf(counter3));
            }
        });


        // Pasamos con la fecha de ida del vuelo.

        departureDate = view.findViewById(R.id.go_to_date);
        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Aquí instanciaremos las variables de día, mes y año para que reciban los
                // respectivos valores del calendario.
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);

                                // La fecha seleccionada tiene que ser mayor que la actual.
                                // Por ello empleamos c, que obtiene la fecha actual desde nuestro
                                // dispositivo.
                                if (selectedDate.after(c)) {
                                    departureDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                } else {
                                    departureDate.setText("");
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Botón para finalizar la búsqueda. Empleará los datos

        Button searchButton = view.findViewById(R.id.button_search);

        // ...

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    // Todos los campos están completos, proceder con la búsqueda

                    pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.

                    // Configurar los datos en la instancia de FlightInfo
                    goToData.setOrigin(originAirport);
                    goToData.setDestination(destinationAirport);
                    goToData.setAdults(counter);
                    goToData.setKids(counter2);
                    goToData.setBabies(counter3);
                    goToData.setGoToDate(departureDate.getText().toString());

                    String peticion = "?departure_date=" + goToData.getGoToDate() + " 00:00:00" +
                            "&origin=" + goToData.getOrigin() + "&destination=" + goToData.getDestination();

                    search(peticion);

                    // Impresión por Log para confirmar que sale correctamente el jSON
                    Log.d("Parte de la petición para la url:", peticion);

                } else {
                    // Mostrar popup si algún campo no está lleno
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("¡Alerta! Algunos de los campos no han sido completados.");

                    builder.setNegativeButton("CERRAR", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
            }
        });


        // Para cambiar entre fragmentos:

        Button cambiarAIdaVuelta = view.findViewById(R.id.button_round_trip);
        cambiarAIdaVuelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Empleamos un .navigate con el controller para movernos entre los fragments
                // definidos en el navigation.

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.search_flight, new RoundTrip()).commit();
            }
        });

        return view;
    }

    private String search(String request_url) {
        String url = Server.name + "/flights" + request_url;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("cheapest") && response.has("fastest")) {
                            // Imprimir la respuesta completa en el Logcat
                            pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.

                            Log.d("Respuesta completa", response.toString());

                            // Se inicia SearchResultsActivity
                            Intent intent = new Intent(getContext(), SerchResultsActivity.class);
                            intent.putExtra("jsonFlightInfo", response.toString()); // Aquí se pasan los datos
                            intent.putExtra("adults", Integer.parseInt(adults.getText().toString()));
                            intent.putExtra("kids", Integer.parseInt(kids.getText().toString()));
                            intent.putExtra("babies", Integer.parseInt(babies.getText().toString()));
                            startActivity(intent);

                        } else {

                            pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                            Log.e("Respuesta del servidor", "Las claves 'cheapest' o 'fastest' faltan en la respuesta JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                if (error instanceof NetworkError || error instanceof ServerError) {
                    if (error.networkResponse.statusCode == 404) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Error 404: No se encontraron 3 vuelos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error en la solicitud" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        return url;
    }

    private boolean validateFields() {
        // Validar si los campos están llenos
        return !TextUtils.isEmpty(originAirport) &&
                !TextUtils.isEmpty(destinationAirport) &&
                counter > 0 && counter2 >= 0 && counter3 >= 0 &&
                !TextUtils.isEmpty(departureDate.getText());
    }


}