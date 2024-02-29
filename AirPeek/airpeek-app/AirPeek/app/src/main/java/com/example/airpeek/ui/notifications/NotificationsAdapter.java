package com.example.airpeek.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.R;
import com.google.android.material.badge.BadgeDrawable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {
    // Lista de elementos de notificación a presentar
    private List<NotificationsItem> notificationsToBePresented;
    // Actividad donde se mostrará el RecyclerView
    private Activity activity;
    private BadgeDrawable badge;
    private Context context;

    // Constructor del adaptador
    public NotificationsAdapter(BadgeDrawable badge, List<NotificationsItem> notifications, Activity activity) {
        this.badge = badge;
        filterNotifications(notifications);
        this.activity = activity;
    }

    public void setNotificationsList(List<NotificationsItem> notificationsList) {
        filterNotifications(notificationsList);
    }

    private void filterNotifications(List<NotificationsItem> notifications) {
        LocalDateTime now = LocalDateTime.now();
        notificationsToBePresented = new ArrayList<>();
        for (NotificationsItem notification : notifications) {
            long hours = Duration.between(now, notification.getDepartureDateTime()).toHours();
            if (hours <= 24 && hours >= 0) {
                notificationsToBePresented.add(notification);
            }
        }
        if (notificationsToBePresented.size() >= 15) {
            badge.setNumber(15);
            badge.setVisible(true);
        }else if (notificationsToBePresented.size() == 0){
            badge.setVisible(false);
        } else if (notificationsToBePresented.size() > 0) {
            badge.setVisible(true);
            badge.setNumber(notificationsToBePresented.size());
        }
    }
    // Este método se llama cuando se crea un nuevo ViewHolder
    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Inflamos la vista del elemento de notificación
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        // Creamos un nuevo ViewHolder con la vista inflada
        NotificationsViewHolder cellViewHolder = new NotificationsViewHolder(view);
        return cellViewHolder;
    }
    // Este método se llama para vincular los datos con el ViewHolder

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position){
        // Obtenemos los datos en la posición actual
        NotificationsItem dataInPosition = notificationsToBePresented.get(position);
        // Mostramos los datos en el ViewHolder
        holder.showData(dataInPosition);
    }
    // Este método devuelve el número total de elementos en los datos
    @Override
    public int getItemCount(){
        return notificationsToBePresented.size();
    }
}