    package com.example.cornerfinder;

    import android.content.Context;
    import android.os.Bundle;
    import android.view.MenuItem;

    import com.example.cornerfinder.savedplaces.SavedPlacesFragment;
    import com.google.android.material.navigation.NavigationView;

    import com.example.cornerfinder.generalmap.GeneralMapFragment;
    import com.example.cornerfinder.recommended.RecommendedFragment;
    import com.example.cornerfinder.ui.account.AccountFragment;
    import com.example.cornerfinder.routes.Routes;
    import com.example.cornerfinder.ui.editpreferences.EditPreferencesFragment;
    import androidx.activity.OnBackPressedCallback;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.ActionBarDrawerToggle;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.core.view.GravityCompat;
    import androidx.fragment.app.Fragment;
    import androidx.drawerlayout.widget.DrawerLayout;
    import com.example.cornerfinder.summermode.SummerModeFragment;
    import com.example.cornerfinder.ui.hotspots.HotspotsFragment;

    public class MainActivity extends AppCompatActivity {
        private Context context = this;
        private DrawerLayout drawerLayout;
        private Toolbar toolbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {//inicializamos los atributos
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            drawerLayout = findViewById(R.id.drawer_layout);
            toolbar = findViewById(R.id.toolbar);
            // Verificar si la actividad se inició con una acción específica
             if (getIntent().getAction() != null && getIntent().getAction().equals("ACTION_START_ADD_LOCATION")) {
            // Inflar el fragmento AddlocationFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddlocationFragment())
                    .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HotspotsFragment())
                        .commit();

            }
            //creamos metodo que cierre el menu y no la app
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                //metodo que cierra el menu si se pulsa atrás.
                @Override
                public void handleOnBackPressed() {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        if (isEnabled()) {
                            setEnabled(false);
                            MainActivity.super.onBackPressed();
                        }
                    }
                }
            });
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.nav_view);
            //   creamos el elemento que escuchara en cual boton clickamos de nuestro menú
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // menu que lleva a sus actividades
                    Fragment fragment = null;
                    if (item.getItemId() == R.id.nav_hotspots) {
                        fragment = new HotspotsFragment();
                    } else if (item.getItemId() == R.id.nav_recommended) {
                        fragment = new RecommendedFragment();
                    } else if (item.getItemId() == R.id.nav_lugaresguardados) {
                        fragment = new SavedPlacesFragment();
                    } else if (item.getItemId() == R.id.nav_summermode) {
                        fragment = new SummerModeFragment();
                    }else if(item.getItemId() == R.id.nav_lugaresguardados){
                        fragment = new SavedPlacesFragment();
                    } else if (item.getItemId() == R.id.nav_generalmap) {
                        fragment = new GeneralMapFragment();
                    } else if (item.getItemId() == R.id.nav_routes) {
                        fragment = new Routes();
                    } else if (item.getItemId() == R.id.nav_edit_preferences) {
                        fragment = new EditPreferencesFragment();
                    } else if (item.getItemId() == R.id.nav_account) {
                        fragment = new AccountFragment();
                    }else if (item.getItemId() == R.id.nav_accountsettings){
                        fragment = new AccountSettingsFragment();
                    } else if (item.getItemId() == R.id.nav_closesession) {
                        //fragment = new ();
                    }

                    //si no llega ningun fragment
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                    // Cierra el Navigation Drawer después de la selección
                    return false;
                }
            });
        }
    }
