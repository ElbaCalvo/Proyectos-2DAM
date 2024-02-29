package com.example.airpeek.ui.serchresults;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.JsonObjectRequestWithAuthentication;
import com.example.airpeek.R;
import com.example.airpeek.Server;
import com.example.airpeek.ui.serchresults.SerchResultsData;

import org.json.JSONException;
import org.json.JSONObject;

public class SerchResultsViewHolder extends RecyclerView.ViewHolder {
    private RequestQueue requestQueue;
    private TextView textViewOrigin;
    private TextView textViewDestination;
    private TextView textViewDepartureHour;
    private TextView textViewArrivalHour;
    private TextView textViewDepartureDate;
    private TextView textViewArrivalDate;
    private TextView price;
    private Button buy_button;
    private TextView textViewOrigin2;
    private TextView textViewDestination2;
    private TextView textViewDepartureHour2;
    private TextView textViewArrivalHour2;
    private TextView textViewDepartureDate2;
    private TextView textViewArrivalDate2;
    private TextView price2;
    private Button buy_button2;
    private TextView textViewOrigin3;
    private TextView textViewDestination3;
    private TextView textViewDepartureHour3;
    private TextView textViewArrivalHour3;
    private TextView textViewDepartureDate3;
    private TextView textViewArrivalDate3;
    private TextView price3;
    private Button buy_button3;
    private TextView top3;

    public SerchResultsViewHolder(@NonNull View itemView) {
        super(itemView);
        top3 = (TextView) itemView.findViewById(R.id.top3);
        textViewOrigin = (TextView) itemView.findViewById(R.id.origin);
        textViewDestination = (TextView) itemView.findViewById(R.id.destination);
        textViewDepartureHour = (TextView) itemView.findViewById(R.id.origin_hour);
        textViewArrivalHour = (TextView) itemView.findViewById(R.id.destination_hour);
        textViewDepartureDate = (TextView) itemView.findViewById(R.id.departure_date);
        textViewArrivalDate = (TextView) itemView.findViewById(R.id.arrival_date);
        price = (TextView) itemView.findViewById(R.id.price);
        buy_button = (Button) itemView.findViewById(R.id.button);

        textViewOrigin2 = (TextView) itemView.findViewById(R.id.origin2);
        textViewDestination2 = (TextView) itemView.findViewById(R.id.destination2);
        textViewDepartureHour2 = (TextView) itemView.findViewById(R.id.origin_hour2);
        textViewArrivalHour2 = (TextView) itemView.findViewById(R.id.destination_hour2);
        textViewDepartureDate2 = (TextView) itemView.findViewById(R.id.departure_date2);
        textViewArrivalDate2 = (TextView) itemView.findViewById(R.id.arrival_date2);
        price2 = (TextView) itemView.findViewById(R.id.price2);
        buy_button2 = (Button) itemView.findViewById(R.id.button2);

        textViewOrigin3 = (TextView) itemView.findViewById(R.id.origin3);
        textViewDestination3 = (TextView) itemView.findViewById(R.id.destination3);
        textViewDepartureHour3 = (TextView) itemView.findViewById(R.id.origin_hour3);
        textViewArrivalHour3 = (TextView) itemView.findViewById(R.id.destination_hour3);
        textViewDepartureDate3 = (TextView) itemView.findViewById(R.id.departure_date3);
        textViewArrivalDate3 = (TextView) itemView.findViewById(R.id.arrival_date3);
        price3 = (TextView) itemView.findViewById(R.id.price3);
        buy_button3 = (Button) itemView.findViewById(R.id.button3);

        this.requestQueue = Volley.newRequestQueue(itemView.getContext());
    }

    public void bindBuyButton(String url, String url2, String url3, String id , String id2, String id3) {
        Context context = buy_button.getContext();

        // Se obtienen las preferencias compartidas para el token de sesión
        SharedPreferences preferences = context.getSharedPreferences("AIRPEEK_APP_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);

        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se abre la URL de compra en el navegador predeterminado
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);

                if (sessionToken != null) {
                    // Si hay un token de sesión, se llama a la función para agregar el vuelo a los vuelos del usuario
                    flightToUserFlights(id, sessionToken, v.getContext());
                } else {
                    // Manejar el caso donde el token de sesión no está disponible
                }
            }
        });

        buy_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se abre la URL de compra en el navegador predeterminado
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url2));
                v.getContext().startActivity(i);

                if (sessionToken != null) {
                    // Si hay un token de sesión, se llama a la función para agregar el vuelo a los vuelos del usuario
                    flightToUserFlights(id2, sessionToken, context);
                } else {
                    // Manejar el caso donde el token de sesión no está disponible
                }
            }
        });

        buy_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se abre la URL de compra en el navegador predeterminado
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url3));
                v.getContext().startActivity(i);

                if (sessionToken != null) {
                    // Si hay un token de sesión, se llama a la función para agregar el vuelo a los vuelos del usuario
                    flightToUserFlights(id3, sessionToken, context);
                } else {
                    // Manejar el caso donde el token de sesión no está disponible
                }
            }
        });
    }

    public void flightToUserFlights(String id, String sessionToken, Context context){
        // Se crea un objeto JSON para la solicitud
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("sessionToken", sessionToken);
            jsonRequest.put("flightId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Se crea una solicitud JSON para agregar el vuelo a los vuelos del usuario
        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication
                (Request.Method.PUT,
                        Server.name + "/user/flights/" + id,
                        jsonRequest,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Se guarda la respuesta JSON
                                saveJsonToPreferences(context, "KEY_JSON_RESPONSE", response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                },
                        context
                );
        requestQueue.add(request);
    }

    private void saveJsonToPreferences(Context context, String key, String json) {
        // Se obtienen las preferencias compartidas de la aplicación
        SharedPreferences preferences = context.getSharedPreferences("AIRPEEK_APP_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit(); // Editor para modificar las preferencias
        editor.putString(key, json);  // Se agrega la cadena JSON con la clave proporcionada
        editor.apply(); // Se aplican los cambios
    }

    public void showData(SerchResultsData savedData, String title){
        textViewOrigin.setText(savedData.getOrigin());
        textViewDestination.setText(savedData.getDestination());
        textViewDepartureHour.setText(savedData.getDepartureHour());
        textViewArrivalHour.setText(savedData.getArrivalHour());
        textViewDepartureDate.setText(savedData.getDepartureDate());
        textViewArrivalDate.setText(savedData.getArrivalDate());
        price.setText(savedData.getPrice());
        textViewOrigin2.setText(savedData.getOrigin2());
        textViewDestination2.setText(savedData.getDestination2());
        textViewDepartureHour2.setText(savedData.getDepartureHour2());
        textViewArrivalHour2.setText(savedData.getArrivalHour2());
        textViewDepartureDate2.setText(savedData.getDepartureDate2());
        textViewArrivalDate2.setText(savedData.getArrivalDate2());
        price2.setText(savedData.getPrice2());
        textViewOrigin3.setText(savedData.getOrigin3());
        textViewDestination3.setText(savedData.getDestination3());
        textViewDepartureHour3.setText(savedData.getDepartureHour3());
        textViewArrivalHour3.setText(savedData.getArrivalHour3());
        textViewDepartureDate3.setText(savedData.getDepartureDate3());
        textViewArrivalDate3.setText(savedData.getArrivalDate3());
        price3.setText(savedData.getPrice3());

        top3.setText(title);
    }
}
