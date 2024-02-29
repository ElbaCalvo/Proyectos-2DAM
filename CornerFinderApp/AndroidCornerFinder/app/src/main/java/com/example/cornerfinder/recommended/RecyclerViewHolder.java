package com.example.cornerfinder.recommended;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cornerfinder.R;
import com.example.cornerfinder.generalmap.GeneralMapFragment;
import com.example.cornerfinder.summermode.Util;

// Clase que representa un ViewHolder para el RecyclerView
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    // Variables para los elementos de la vista
    private TextView place_name;
    private TextView tag;
    private TextView description;
    private ImageView imageView;
    private Button location;

    // Constructor del ViewHolder
    public RecyclerViewHolder(@NonNull View ivi){
        super(ivi);
        // Encontrar los elementos de la vista
        place_name = ivi.findViewById(R.id.place_name);
        tag = ivi.findViewById(R.id.tag);
        description = ivi.findViewById(R.id.description);
        imageView = ivi.findViewById(R.id.image_view);
        location = ivi.findViewById(R.id.location);
    }

    // Método para mostrar los datos en los elementos de la vista
    public void showData(RecyclerItems items){
        // Establecer el texto de los TextViews y la imagen del ImageView
        place_name.setText(items.getPlace_Name());
        description.setText(items.getDescription());
        tag.setText(items.getTag());
        Util.downloadBitmapToImageView(items.getImage_url(), this.imageView);

        // Establecer un OnClickListener para el botón de ubicación
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            // Comprobar que funciona bien el botón location y obtiene los datos.
            public void onClick(View v) {
                // Obtener el FragmentManager desde el contexto de la vista
                FragmentManager fragmentManager = ((FragmentActivity)itemView.getContext()).getSupportFragmentManager();

                // Crear una instancia del fragmento GeneralMapFragment
                GeneralMapFragment generalMapFragment = new GeneralMapFragment();

                // Crear un Bundle para pasar datos adicionales al fragmento
                Bundle bundle = new Bundle();
                bundle.putString("location", items.getLocation());
                generalMapFragment.setArguments(bundle);

                // Reemplazar el contenido del contenedor de fragmentos con GeneralMapFragment
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, generalMapFragment)
                        .addToBackStack(null).commit();
            }
        });
    }
}