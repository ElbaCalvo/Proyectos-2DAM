package com.example.airpeek.ui.specialoffers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airpeek.R;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersViewHolder> {
    private List<SpecialOffersData> allTheData;

    // Constructor de la clase SpecialOffersAdapter
    public SpecialOffersAdapter(List<SpecialOffersData> specialOffersDataList, Activity activity) {
        this.allTheData = specialOffersDataList;
    }

    // Método que crea una nueva vista para un elemento de la lista
    @NonNull
    @Override
    public SpecialOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cellView = inflater.inflate(R.layout.fragment_special_offers, parent, false);
        return new SpecialOffersViewHolder(cellView);
    }

    // Método que asocia los datos de un elemento de la lista a una vista
    @Override
    public void onBindViewHolder(@NonNull SpecialOffersViewHolder holder, int position){
        SpecialOffersData dataForThisCell = this.allTheData.get(position);
        holder.showData(dataForThisCell);
        holder.bindBuyButton(dataForThisCell.getBuyUrl(), dataForThisCell.getId());
    }

    // Método que devuelve la cantidad de elementos en la lista de datos
    @Override
    public int getItemCount(){
        return this.allTheData.size();
    }
}
