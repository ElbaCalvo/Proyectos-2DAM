package com.example.cornerfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    // Declaración de variables
    private Button registerButton, loginButton, accessButton;
    private EditText textUsername, textPassword;
    private Context context = this;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de los elementos
        mAuth = FirebaseAuth.getInstance();
        accessButton = findViewById(R.id.access_button);
        registerButton = findViewById(R.id.register_page);
        loginButton = findViewById(R.id.login_page);
        textUsername = findViewById(R.id.campo_email);
        textPassword = findViewById(R.id.campo_contraseña);


        // Hacer que cuando se pulse el botón de registro te lleve a la pantalla de RegisterActivity.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        accessButton.setOnClickListener(v -> loginUser());
    }

    // Método para iniciar sesión con correo electrónico y contraseña
    private void loginUser() {
        // Obtención de los datos
        String email = textUsername.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        // Iniciar sesión con el método signInWithEmailAndPassword de FirebaseAuth.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) { // Si el inicio de sesión es exitoso, se inicia la actividad principal y se finaliza la actividad actual
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else { // Si el inicio de sesión falla
                        Toast.makeText(context, "Uno de los campos es inválido. ERROR:"+ task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


