package com.mohammed.babelrestaurant.screen_intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohammed.babelrestaurant.utils.Constants;
import com.mohammed.babelrestaurant.views.HomeActivity;
import com.mohammed.babelrestaurant.databinding.ActivityIntroBinding;
import com.mohammed.babelrestaurant.utils.ChangeLocaleLanguageHelper;
import com.mohammed.babelrestaurant.utils.SharedPreferencesHelper;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        String language = SharedPreferencesHelper.getLanguagePreferences(Constants.LANGUAGE_KEY, this);
        ChangeLocaleLanguageHelper.setLocaleLanguage(this, language);
        setContentView(binding.getRoot());

        // Displaying edge-to-edge
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return;
        }
        startActivity(new Intent(IntroActivity.this, HomeActivity.class));
        finish();
    }


}