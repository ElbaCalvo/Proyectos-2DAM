package com.example.cornerfinder.ui.editpreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornerfinder.databinding.FragmentEditPreferencesBinding;

public class EditPreferencesFragment extends Fragment {

    private FragmentEditPreferencesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Creación del ViewModel para este fragmento
        EditPreferencesViewModel editPreferencesViewModel =
                new ViewModelProvider(this).get(EditPreferencesViewModel.class);

        // Inflar la vista del fragmento usando el binding
        binding = FragmentEditPreferencesBinding.inflate(inflater, container, false);
        // Obtener la vista raíz del fragmento
        View root = binding.getRoot();

        final TextView textView = binding.textEditPreferences;
        // Observar los cambios en el texto del ViewModel y actualizar el TextView cuando cambie

        editPreferencesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    // Método que se llama cuando la vista del fragmento está a punto de ser destruida

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar la referencia al binding para evitar fugas de memoria
        binding = null;
    }
}
