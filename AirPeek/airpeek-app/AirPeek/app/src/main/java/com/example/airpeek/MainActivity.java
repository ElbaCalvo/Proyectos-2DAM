package com.example.airpeek;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.airpeek.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Estas siguientes líneas, definimos el inflado del diseño de la actividad y lo
        // relacionamos con una variable que actue de enlace entre el Navigation y los fragmentos
        // o las actividades.

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view); // Definimos el navView y lo identificamos.


        // Configuramos la barra de la app y su constructor, procurando que todos los items que
        // usemos, o, en este caso, los fragments, estén definidos en el interior.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search_flights, R.id.navigation_special_offers,
                R.id.navigation_notifications, R.id.navigation_profile, R.id.navigation_user_flights).build();

        // Encontrar el controlador de navegación y asignarlo a una variable

        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main);
        // Configurar la barra de la aplicación y la vista de navegación con el controlador de navegación
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }
    // Método que se llama para inflar el menú de la actividad

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        // Encontrar el ítem de información y cambiar el color de su ícono a blanco

        MenuItem infoItem = menu.findItem(R.id.action_info);
        Drawable infoIcon = infoItem.getIcon();
        if (infoIcon != null) {
            infoIcon = infoIcon.mutate();
            infoIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }
    // Método que se llama cuando se selecciona un ítem del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Si el ítem seleccionado es el ítem de información, iniciar InfoActivity
        if (item.getItemId() == R.id.action_info) {
            // Aquí puedes abrir tu pantalla de información
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}