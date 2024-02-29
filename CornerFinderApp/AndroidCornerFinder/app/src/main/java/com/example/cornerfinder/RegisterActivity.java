package com.example.cornerfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

// Clase para la actividad de registro
public class RegisterActivity extends AppCompatActivity {
    // Declaración de variables para los elementos de la interfaz de usuario
    private EditText usernameEditText, passwordEditText, password2EditText, emailEditText, birthdateEditText;
    private Context context = this;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private FirebaseAuth mAuth;
    private Button registerPage, loginPage, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicialización de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Asignación de elementos de la interfaz de usuario a las variables
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        password2EditText = findViewById(R.id.password2);
        emailEditText = findViewById(R.id.email);
        birthdateEditText = findViewById(R.id.birthdate);

        // Configuración del DatePickerDialog para seleccionar la fecha de nacimiento
        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        dateSetListener,
                        year, month, day);
                dialog.show();
            }
        });

        // Configuración del listener para el DatePickerDialog
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1; // Month is 0-based, so we add 1
                String strMonth;
                if (month < 10) {
                    strMonth = "0" + month;
                } else {
                    strMonth = String.valueOf(month);
                }
                String date = day + "-" + strMonth + "-" + year;
                birthdateEditText.setText(date);
            }
        };

        // Asignación de botones a las variables
        registerButton = findViewById(R.id.register_button);
        registerPage = findViewById(R.id.register_page);
        loginPage = findViewById(R.id.login_page);

        // Configuración del listener para el botón de registro
        registerButton.setOnClickListener(v -> registerUser());

        // Configuración del listener para el botón de inicio de sesión
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para registrar al usuario
    private void registerUser(){
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String password2 = password2EditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String birthdate = birthdateEditText.getText().toString().trim();
        birthdateEditText.setKeyListener(null);

        // Validación de los datos de registro
        if (validateRegister(username,password,password2,email,birthdate)){
            // Creación del usuario en Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task-> {
                        if (task.isSuccessful() ) {
                            // Si la creación del usuario es exitosa, se guardan los datos del usuario en la base de datos
                            FirebaseUser user = mAuth.getCurrentUser();
                            Usuario nuevoUser = new Usuario(username, email, birthdate);
                            FirebaseDatabase.getInstance().getReference("usuarios")
                                    .child(user.getUid())
                                    .setValue(nuevoUser)
                                    .addOnCompleteListener(taskDb -> {
                                        if (taskDb.isSuccessful()) {
                                            // Si los datos se guardan correctamente, se muestra un mensaje y se redirige al usuario a la pantalla de inicio de sesión
                                            Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // Si hay un error al guardar los datos, se muestra un mensaje con el error
                                            Toast.makeText(context, "Error al guardar datos:"+taskDb.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Si hay un error al crear el usuario, se muestra un mensaje con el error
                            Log.e("TagError", task.getException().getMessage());
                            Toast.makeText(context, "Registro fallido"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    // Método para validar los datos de registro
    private boolean validateRegister(String username, String password,String password2, String email, String birthdate){
        // Verificación de que todos los campos estén llenos
        if (username.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty() ||birthdate.isEmpty()){
            Toast.makeText(this, "Uno/s de los campos no han sido completados!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Verificación de que las contraseñas coincidan
        if (!password.equals(password2)){
            Toast.makeText(this, "Registro fallido, las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            passwordEditText.setError("Las contraseñas deben coincidir");
            password2EditText.setError("Las contraseñas deben coincidir");
            return false;
        }
        // Verificación del formato del email
        if (!email.contains("@") || email.length() < 8){
            emailEditText.setError("Formato inválido de email");
            return false;
        }
        // Verificación del formato de la fecha de nacimiento
        if (birthdate.length() != 10){
            birthdateEditText.setError("Formato inválido de fecha de nacimiento");
            return false;
        }
        return true;
    }

    // Clase para el objeto Usuario
    static class Usuario {
        public String username, email, birthdate;

        public Usuario(String username, String email, String birthdate) {
            this.username = username;
            this.email = email;
            this.birthdate = birthdate;
        }
    }

}