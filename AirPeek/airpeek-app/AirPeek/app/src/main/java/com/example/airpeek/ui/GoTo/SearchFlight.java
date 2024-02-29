package com.example.airpeek.ui.GoTo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airpeek.R;

// Fragmento que servirá como contenedor para que el fragmento de Ida y el de IdaVuelta puedan
// mostrarse correctamente por pantalla.
public class SearchFlight extends Fragment {

    public SearchFlight(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_flight, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_flight, new GoTo()).commit();
    }


}
