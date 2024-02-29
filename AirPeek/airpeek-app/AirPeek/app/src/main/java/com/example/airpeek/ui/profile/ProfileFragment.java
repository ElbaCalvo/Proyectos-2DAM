package com.example.airpeek.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.airpeek.EditProfileActivity;
import com.example.airpeek.JsonObjectRequestWithAuthentication;
import com.example.airpeek.LoginActivity;
import com.example.airpeek.MainActivity;
import com.example.airpeek.R;
import com.example.airpeek.Server;
import com.example.airpeek.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private RequestQueue requestQueue;
    private String imageUrl;
    private ProgressBar pb1;

    Context context = getContext();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Aquí se muestra el layout del fragmento y se enlazan los elementos del mismo
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inicar la Actividad EditProfileActivity al hacer click en el botón
        pb1 = root.findViewById(R.id.loadingScreen);
        binding.registerButton.setOnClickListener(v -> {
            pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
            Intent intent = new Intent(v.getContext(), EditProfileActivity.class);
            context.startActivity(intent);
        });
        binding.deleteLabel.setOnClickListener(v -> {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            context = getContext();
            // Crea un diálogo de confirmación al hacer click en el botón
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("¿Estás seguro de querer eliminar la cuenta?");

            builder.setPositiveButton("SÍ", (dialog, which) -> {
                pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                sendDeleteRequest();

            });
            builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            builder.show();
        });
        binding.logoutLabel.setOnClickListener(v -> {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            context = getContext();
            // Crea un diálogo de confirmación al hacer click en el botón
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("¿Estás seguro de querer cerrar sesión?");

            builder.setPositiveButton("SÍ", (dialog, which) -> {
                pb1.setVisibility(View.VISIBLE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                sendLogoutRequest();
            });
            builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            builder.show();
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        context = getContext();
        sendProfileRequest();
    }

    private void sendProfileRequest() {
        // Realiza la petición mandando el token en headers y coloca los valores recibidos en sus elementos
        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.GET,
                Server.name + "/user",
                null,
                response -> {
                    try {
                        Profile profile = new Profile(response);
                        binding.usernameLabel.setText(profile.getName());
                        binding.emailLabel.setText(profile.getEmail());
                        binding.countryLabel.setText(profile.getEmail());
                        binding.birthdateLabel.setText(profile.getCountry());
                        // Comprueba que el enlace de la foto de perfil no está vacío o es nulo y lo muestra
                        if (!Objects.equals(profile.getImage(), "")){
                            Picasso.get().load(profile.getImage()).into(binding.profilePicture);
                        }
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    if (error.networkResponse == null) {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_LONG).show();
                    } else {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        int serverCode = error.networkResponse.statusCode;
                        Toast.makeText(context, "Estado de respuesta: " + serverCode, Toast.LENGTH_LONG).show();
                    }

                },
                context
        );
        this.requestQueue.add(request);
    }

    private void sendDeleteRequest() {
        // Realiza la petición mandando el token en headers y coloca los valores recibidos en sus elementos
        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.DELETE,
                Server.name + "/user",
                null,
                response -> {
                    pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                    Toast.makeText(context, "Se ha eliminado la cuenta", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    },
                error -> {
                    if (error.networkResponse == null) {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_LONG).show();
                    } else {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        int serverCode = error.networkResponse.statusCode;
                        Toast.makeText(context, "Estado de respuesta: " + serverCode, Toast.LENGTH_LONG).show();
                    }

                },
                context
        );
        this.requestQueue.add(request);
    }

    private void sendLogoutRequest() {
        // Realiza la petición mandando el token en headers y coloca los valores recibidos en sus elementos
        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.DELETE,
                Server.name + "/user/session",
                null,
                response -> {
                    pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                    Toast.makeText(context, "Se ha cerrado la sesión", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    },
                error -> {
                    if (error.networkResponse == null) {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_LONG).show();
                    } else {
                        pb1.setVisibility(View.GONE); // Alternamos entre la visibilidad de la barra de progresión a nuestra conveniencia.
                        int serverCode = error.networkResponse.statusCode;
                        Toast.makeText(context, "Estado de respuesta: " + serverCode, Toast.LENGTH_LONG).show();
                    }

                },
                context
        );
        this.requestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
