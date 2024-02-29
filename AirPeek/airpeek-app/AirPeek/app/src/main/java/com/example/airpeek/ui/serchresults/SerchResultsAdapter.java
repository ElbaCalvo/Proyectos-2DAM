package com.example.airpeek.ui.serchresults;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airpeek.R;

import java.util.List;

public class SerchResultsAdapter extends RecyclerView.Adapter<SerchResultsViewHolder> {
    private List<SerchResultsData> saved;
    private Activity activity;

    // Se crea el constructor de la clase SerchResultsAdapter
    public SerchResultsAdapter(List<SerchResultsData> dataSet, Activity activity) {
        this.saved = dataSet;
        this.activity = activity;
    }

    // El método llamado cuando se necesita crear una nueva ViewHolder
    @NonNull
    @Override
    public SerchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Se infla el diseño de la celda desde el XML
        View savedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_search_results, parent, false);

        // Se devuelve una nueva instancia de SerchResultsAdapter
        return new SerchResultsViewHolder(savedView);
    }

    // El método llamado para actualizar una ViewHolder existente con datos específicos
    @Override
    public void onBindViewHolder(@NonNull SerchResultsViewHolder holder, int position){
        // Se muestran los datos en la celda
        SerchResultsData dataForThisCell = saved.get(position);

        // Decide qué texto mostrar basado en la posición
        String text;
        if (position < saved.size() / 2) {
            text = "Top 3 más baratos";
        } else {
            text = "Top 3 más rápidos";
        }

        holder.bindBuyButton(dataForThisCell.getBuyUrl(), dataForThisCell.getBuyUrl2(), dataForThisCell.getBuyUrl3(), dataForThisCell.getId(), dataForThisCell.getId2(), dataForThisCell.getId3());
        holder.showData(dataForThisCell, text);
    }

    @Override
    // El método llamado para obtener el número total de elementos en el conjunto de datos
    public int getItemCount(){
        return saved.size();
    }
}
