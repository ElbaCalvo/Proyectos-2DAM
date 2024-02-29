package com.example.cornerfinder.ui.account;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cornerfinder.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> {
    private List<AccountData> savedplaces;
    private Activity activity;

    // Constructor que recibe la lista de datos del SummerModeFragment y la actividad.
    public AccountAdapter(List<AccountData> dataSet, Activity activity){
        this.savedplaces=dataSet;
        this.activity=activity;
    }

    // Crea y devuelve un nuevo ViewHolder inflando el diseño de celda para que se visualice.
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View accountView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cell_recently_added, parent, false);
        return new AccountViewHolder(accountView);
    }

    // Asocia los datos de la posición actual a la vista del ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position){
        AccountData dataForThisCell = savedplaces.get(position);
        holder.showData(dataForThisCell);
    }

    // Devuelve la cantidad total de elementos en el conjunto de datos.
    @Override
    public int getItemCount(){ return savedplaces.size(); }
}
