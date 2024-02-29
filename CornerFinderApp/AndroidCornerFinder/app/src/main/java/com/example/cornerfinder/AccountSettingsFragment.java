package com.example.cornerfinder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;

public class AccountSettingsFragment extends Fragment {

    // Declaración de variables
    private EditText usserEditText;
    private EditText passwordEditText;
    private EditText password2EditText;
    private EditText emailEditText;
    private EditText birthdateEditText;
    private Button changesButton;

    public AccountSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar la vista del Fragmento
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        // Inicialización de los elementos de la interfaz de usuario
        usserEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        password2EditText = view.findViewById(R.id.password2);
        emailEditText = view.findViewById(R.id.email);
        birthdateEditText = view.findViewById(R.id.birthdate);
        changesButton = view.findViewById(R.id.confirm_button);

        // Configuración del DatePickerDialog para seleccionar la fecha de nacimiento al clickar el campo.
        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month, day;
                                // Si el mes o el día es menor a 10 se le añade un 0 delante ("05/03/2000")
                                if (monthOfYear < 10){
                                    month = "0" + (monthOfYear + 1);
                                } else {
                                    month = String.valueOf(monthOfYear + 1);
                                }
                                if (dayOfMonth < 10){
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }
                                if (year < 2024) {
                                    birthdateEditText.setText(year + "-" + month + "-" + day );
                                } else {
                                    birthdateEditText.setText("");
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        changesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se llama al método validateChanges
                validateChanges(usserEditText.getText().toString(), passwordEditText.getText().toString(), password2EditText.getText().toString(), emailEditText.getText().toString(), birthdateEditText.getText().toString());
            }
        });

        return view;
    }

    private void showInvalidFieldDialog() { // Método para mostrar un ventana de diálogo de campo inválido
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Campo inválido");
        builder.setMessage("Uno o más campos son inválidos. Por favor, revisa tus entradas.");

        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private boolean validateChanges(String username, String password,String password2, String email, String birthdate){
        // Verificación de que no hay campos vacíos.
        if (username.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty() || birthdate.isEmpty()){
            showInvalidFieldDialog(); // Se llama al método para mostrar un ventana de diálogo
            return false;
        }
        // Verificación de que las contraseñas coinciden.
        if (!password.equals(password2)){
            passwordEditText.setError("La contraseña no coincide");
            password2EditText.setError("La contraseña no coincide");
            showInvalidFieldDialog(); // Se llama al método para mostrar un ventana de diálogo
            return false;
        }
        // Verificación del formato del correo electrónico.
        if (!email.contains("@") || email.length() < 8){
            emailEditText.setError("Formato inválido de email");
            showInvalidFieldDialog(); // Se llama al método para mostrar un ventana de diálogo
            return false;
        }
        return true;
    }
}
