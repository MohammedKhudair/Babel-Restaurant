package com.mohammed.babelrestaurant.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.databinding.ActivityHomeBinding;
import com.mohammed.babelrestaurant.utils.ChangeLocaleLanguageHelper;
import com.mohammed.babelrestaurant.utils.Constants;
import com.mohammed.babelrestaurant.utils.SharedPreferencesHelper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        String language = SharedPreferencesHelper.getLanguagePreferences(Constants.LANGUAGE_KEY, this);
        ChangeLocaleLanguageHelper.setLocaleLanguage(this, language);
        setDarkMode();
        setContentView(binding.getRoot());

        // SetUp BottomNavigationView
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        // Hid and show bottomNavigation based on Fragment
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.ordersHistoryFragment || id == R.id.categoryListFragment ||
                    id == R.id.detailsFragment || id == R.id.addressFragment || id == R.id.orderDetailsFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setDarkMode() {
        String darkMode = SharedPreferencesHelper.getDarkModePreferences(Constants.DARK_MODE_KEY, this);
        switch (darkMode) {
            case Constants.LIGHT_MODE: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            break;
            case Constants.DARK_MODE: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            break;
            default: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }
    }
}