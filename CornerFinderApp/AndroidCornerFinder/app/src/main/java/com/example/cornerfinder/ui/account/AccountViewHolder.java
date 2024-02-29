package com.example.cornerfinder.ui.account;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cornerfinder.R;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private TextView placeName;
    private TextView descripcion;
    private ImageView imageView;

    // Constructor que inicializa las vistas utilizando el diseño de celda:
    public AccountViewHolder(@NonNull View ivi){
        super(ivi);
        placeName = ivi.findViewById(R.id.placeName);
        descripcion = ivi.findViewById(R.id.description);
        imageView = ivi.findViewById(R.id.image_view);
    }

    // Método para mostrar los datos en las vistas correspondientes:
    public void showData(AccountData accountData){
        placeName.setText(accountData.getPlaceName());
        descripcion.setText(accountData.getDescripcion());
        Util.downloadBitmapToImageView(accountData.getImage_url(), this.imageView);
    }
}
