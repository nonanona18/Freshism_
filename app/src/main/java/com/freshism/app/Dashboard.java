package com.freshism.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.freshism.app.databinding.ActivityDashboardBinding;
import com.freshism.app.ui.dashboard.DashboardFragment;
import com.freshism.app.ui.home.HomeFragment;
import com.freshism.app.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.containerCustomer, new HomeFragment()).commit();

        binding.navViewCustomer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerCustomer, new HomeFragment()).commit();
                    return true;
                } else if (itemId == R.id.navSearch) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerCustomer, new DashboardFragment()).commit();
                    return true;
                } else if (itemId == R.id.navProfile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerCustomer, new ProfileFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
