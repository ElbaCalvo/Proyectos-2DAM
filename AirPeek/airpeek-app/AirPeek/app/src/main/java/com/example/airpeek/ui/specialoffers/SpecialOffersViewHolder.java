package com.example.airpeek.ui.specialoffers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpecialOffersViewHolder extends RecyclerView.ViewHolder {
    private RequestQueue requestQueue;
    private final TextView textViewOrigin;
    private final ImageView imageViewArrow;
    private final TextView textViewDestination;
    private final TextView textViewOriginHour;
    private final TextView textViewArrivalHour;
    private final TextView textViewOriginDate;
    private final TextView textViewArrivalDate;
    private final TextView textViewPrice;
    private final Button BuyButton;

    public SpecialOffersViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewOrigin = (TextView) itemView.findViewById(R.id.origin);
        imageViewArrow = (ImageView) itemView.findViewById(R.id.arrow);
        textViewDestination = (TextView) itemView.findViewById(R.id.destination);
        textViewOriginHour = (TextView) itemView.findViewById(R.id.origin_hour);
        textViewArrivalHour = (TextView) itemView.findViewById(R.id.arrival_hour);
        textViewOriginDate = (TextView) itemView.findViewById(R.id.origin_date);
        textViewArrivalDate = (TextView) itemView.findViewById(R.id.arrival_date);
        textViewPrice = (TextView) itemView.findViewById(R.id.price);
        BuyButton = (Button) itemView.findViewById(R.id.buy_button);

        this.requestQueue = Volley.newRequestQueue(itemView.getContext());
    }

    public void bindBuyButton(String url, String id) {
        Context context = BuyButton.getContext();

        // Se obtienen las preferencias compartidas para el token de sesión
        SharedPreferences preferences = context.getSharedPreferences("AIRPEEK_APP_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);

        BuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se abre la URL de compra en el navegador predeterminado
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);

                if (sessionToken != null) {
                    // Si hay un token de sesión, se llama a la función para agregar el vuelo a los vuelos del usuario
                    flightToUserFlights(id, sessionToken, context);
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

    // Método para mostrar los datos
    public void showData(SpecialOffersData data) {
        this.textViewOrigin.setText(data.getOrigin());
        this.textViewDestination.setText(data.getDestination());
        this.textViewOriginHour.setText(data.getOriginHour());
        this.textViewArrivalHour.setText(data.getArrivalHour());
        this.textViewOriginDate.setText(data.getOriginDate());
        this.textViewArrivalDate.setText(data.getArrivalDate());
        this.textViewPrice.setText(data.getPrice());
    }
}
