package com.mohammed.babelrestaurant.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.sendButton.setOnClickListener(view -> resetPassword());
    }

    private void resetPassword() {
        String email = binding.editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getString(R.string.textBoxNote), Toast.LENGTH_LONG).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Email send", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

            }
            binding.progressBar.setVisibility(View.INVISIBLE);
        });

    }
}