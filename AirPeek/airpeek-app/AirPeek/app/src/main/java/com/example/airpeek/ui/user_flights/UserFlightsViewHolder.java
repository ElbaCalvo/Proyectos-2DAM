package com.example.airpeek.ui.user_flights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.JsonObjectRequestWithAuthentication;
import com.example.airpeek.MainActivity;
import com.example.airpeek.R;
import com.example.airpeek.Server;

public class UserFlightsViewHolder extends RecyclerView.ViewHolder{
    private TextView originTextView;
    private TextView destinationTextView;
    private TextView departureTimeTextView;
    private TextView departureDateTextView;
    private TextView arrivalTimeTextView;
    private TextView arrivalDateTextView;
    private ImageView cross;
    private Context context;
    private RequestQueue requestQueue;
    private UserFlight userFlight;
    public UserFlightsViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        originTextView = itemView.findViewById(R.id.origin);
        destinationTextView = itemView.findViewById(R.id.destination);
        departureTimeTextView = itemView.findViewById(R.id.departure_time);
        departureDateTextView = itemView.findViewById(R.id.departure_date);
        arrivalDateTextView = itemView.findViewById(R.id.arrival_date);
        arrivalTimeTextView = itemView.findViewById(R.id.arrival_time);
        cross = itemView.findViewById(R.id.image_view);
        cross.setOnClickListener(v -> {
            // Crea un diálogo de confirmación
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("¿Estás seguro de querer eliminar el billete?");
            builder.setPositiveButton("SÍ", (dialog, which) -> {
                sendDeleteRequest(userFlight.getFlightId());
            });
            builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            builder.show();
        });
        itemView.setOnClickListener(view -> {
        });
    }

    public void showUserFlight(UserFlight flight) {
        // Inserta el texto en los elementos del layout
        this.originTextView.setText(flight.getOrigin());
        this.destinationTextView.setText(flight.getDestination());
        this.arrivalTimeTextView.setText(flight.getArrivalTime());
        this.arrivalDateTextView.setText(flight.getArrivalDate());
        this.departureTimeTextView.setText(flight.getDepartureTime());
        this.departureDateTextView.setText(flight.getDepartureDate());
        this.userFlight=flight;
    }

    private void sendDeleteRequest(String flightId) {
        // Realiza la petición para eliminarlo si se hace click en la "x"
        requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.DELETE,
                Server.name + "/user/flights/" + flightId,
                null,
                response -> {
                    Toast.makeText(context, "Se ha eliminado el billete", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, context.getClass());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    },
                error -> {
                    if (error.networkResponse == null) {
                        Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_LONG).show();
                    } else {
                        int serverCode = error.networkResponse.statusCode;
                        Toast.makeText(context, "Estado de respuesta: " + serverCode, Toast.LENGTH_LONG).show();
                    }

                },
                context
        );
        this.requestQueue.add(request);

    }


}
