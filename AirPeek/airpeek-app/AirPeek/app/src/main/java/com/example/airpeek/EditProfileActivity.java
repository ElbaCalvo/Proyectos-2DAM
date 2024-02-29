package com.example.airpeek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText password2EditText;

    private EditText emailEditText;
    private Button registerButton;
    private String pictureUrl = null;
    private RequestQueue requestQueue;
    private Context context = this;
    private ProgressBar pb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        nameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        password2EditText = findViewById(R.id.password2);
        emailEditText = findViewById(R.id.email);
        pb1 = findViewById(R.id.loadingScreen);
        ShapeableImageView profilePicture = findViewById(R.id.profile_picture);
        ShapeableImageView editPicture = findViewById(R.id.edit_picture);

        View.OnClickListener imageClickListener = v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setTitle("Enter Text");

            final EditText input = new EditText(EditProfileActivity.this);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                pictureUrl = input.getText().toString();
                if (!Objects.equals(pictureUrl, "")){
                    Picasso.get().load(pictureUrl).into(profilePicture);
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        };

        profilePicture.setOnClickListener(imageClickListener);
        editPicture.setOnClickListener(imageClickListener);

        registerButton = findViewById(R.id.register_button);
        requestQueue = Volley.newRequestQueue(this);
        registerButton.setOnClickListener(v -> {
            String username = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String password2 = password2EditText.getText().toString();
            String email = emailEditText.getText().toString();
            if (validateRegister(username,password,password2, email)){
                pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                sendRegisterRequest(username, password, email, pictureUrl);
            }
        });

    }
    private boolean validateRegister(String username, String password,String password2, String email){
        if (username.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Debes rellenar todos los campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(password2)){
            passwordEditText.setError("La contraseña no coincide");
            password2EditText.setError("La contraseña no coincide");
            return false;
        }
        if (!email.contains("@") || email.length() < 8){
            emailEditText.setError("Formato inválido de email");
            return false;
        }
        return true;
    }
    private void sendRegisterRequest(String username, String password,String email, String picture) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", username);
            requestBody.put("password", password);
            requestBody.put("email", email);
            requestBody.put("image", picture);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.PUT,
                Server.name + "/user",
                requestBody,
                response -> {
                    pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.

                },
                error -> {
                    if (error.networkResponse == null) {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_LONG).show();
                    } else {
                        if (error.networkResponse.statusCode == 409) {
                            pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                            Toast.makeText(context, "El email ya ha sido registrado", Toast.LENGTH_LONG).show();
                        } else {
                            pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(context, "Estado de respuesta: " + serverCode, Toast.LENGTH_LONG).show();
                        }
                    }

                },
                context
        );
        this.requestQueue.add(request);
    }
}

