package com.mrprogrammer.mrtower.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mrprogrammer.mrtower.Firebase.SyncKey;
import com.mrprogrammer.mrtower.R;
import com.mrprogrammer.mrtower.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {
    private ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncFirebase();
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_base2);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void syncFirebase(){
        SyncKey.Companion.syncKey();
    }
}