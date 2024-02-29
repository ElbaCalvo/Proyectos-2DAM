package com.example.airpeek.ui.notifications;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airpeek.R;
import java.time.Duration;
import java.time.LocalDateTime;

public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    private final TextView textview;
    private final TextView descview;
    private LocalDateTime now;
    private long hours;
    // Constructor del ViewHolder
    public NotificationsViewHolder(@NonNull View itemView) {
        super(itemView);
        // Inicializamos los TextViews
        textview = (TextView) itemView.findViewById(R.id.notificationText);
        descview = (TextView) itemView.findViewById(R.id.descriptionText);
    }
    // Método para mostrar los datos en el ViewHolder
    public void showData(NotificationsItem dataInPositionToBeRendered) {
        String text = null;
        String desc = null;
        // Obtenemos la hora actual
        now = LocalDateTime.now();
        // Calculamos la cantidad de horas hasta la salida del vuelo
        hours = Duration.between(now, dataInPositionToBeRendered.getDepartureDateTime()).toHours();

        // Si la hora actual es antes de la salida del vuelo y faltan 24 horas o menos para la salida
        if (now.isBefore(dataInPositionToBeRendered.getDepartureDateTime())) {
            // Si falta más de una hora para la salida, mostramos el número de horas
            if (hours >= 1) {
                text = "Tu vuelo "+dataInPositionToBeRendered.getflightId()+" embarca en " + hours + " horas.";
                desc = "Esté atento a las puertas de embarque";
            } else {
                // Si falta menos de una hora para la salida, mostramos el número de minutos
                long minutes = Duration.between(now, dataInPositionToBeRendered.getDepartureDateTime()).toMinutes();
                text = "Tu vuelo  embarca en " + minutes + " minutos.";
                desc = "Esté atento a las puertas de embarque";
            }
        } else if (!now.isBefore(dataInPositionToBeRendered.getDepartureDateTime()) && now.isBefore(dataInPositionToBeRendered.getArrivalDateTime())) {
            // Si el vuelo ya ha salido pero aún no ha llegado, mostramos que el avión ha despegado
            text = "Tu vuelo "+dataInPositionToBeRendered.getflightId()+" ha despegado!";
            desc = "Tenga un buen vuelo";
        } else if (now.isAfter(dataInPositionToBeRendered.getArrivalDateTime())) {
            // Si el vuelo ya ha llegado, mostramos que el usuario ha aterrizado
            text = "Ha aterrizado el vuelo "+dataInPositionToBeRendered.getflightId()+".";
            desc = "Disfruta de tu destino!";
        }
        // Establecemos el texto y la descripción en los TextViews
        textview.setText(text);
        descview.setText(desc);
    }

}