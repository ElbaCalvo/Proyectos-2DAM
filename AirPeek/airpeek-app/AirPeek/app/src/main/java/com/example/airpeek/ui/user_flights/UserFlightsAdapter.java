package com.example.airpeek.ui.user_flights;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airpeek.R;

public class UserFlightsAdapter extends RecyclerView.Adapter<UserFlightsViewHolder> {
    private UserFlightsList userFlightsToShow;
    public UserFlightsAdapter(UserFlightsList userFlights) {
        this.userFlightsToShow = userFlights;
    }

    @NonNull
    @Override
    public UserFlightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Muestra el recyclerview
        View cellView = inflater.inflate(R.layout.recycler_view_user_flights, parent, false);
        UserFlightsViewHolder cellViewHolder = new UserFlightsViewHolder(cellView);
        return cellViewHolder;
    }
    // Adjudica a cada celda la información que le corresponde
    @Override
    public void onBindViewHolder(@NonNull UserFlightsViewHolder holder, int position) {
        UserFlight dataForThisCell = this.userFlightsToShow.getUserFlights().get(position);
        holder.showUserFlight(dataForThisCell);
    }
    // Cuenta el número de celdas
    @Override
    public int getItemCount() {
        return this.userFlightsToShow.getUserFlights().size();
    }
}
