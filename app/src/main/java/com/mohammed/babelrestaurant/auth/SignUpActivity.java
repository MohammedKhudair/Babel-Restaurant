package com.mohammed.babelrestaurant.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mohammed.babelrestaurant.views.HomeActivity;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.alreadyHaveAccountButton.setOnClickListener(view -> {
            startActivity(new Intent(this, GoogleSignInActivity.class));
            finish();
        });

        binding.registerButton.setOnClickListener(view -> signUpNewAccount());

    }

    private void signUpNewAccount() {
        String name = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.textInputLayoutName.setError(getString(R.string.password_text_box_error));
            return;
        } else {
            binding.textInputLayoutName.setErrorEnabled(false);
        }

        if (!isValidEmail(email)) {
            binding.textInputLayoutEmail.setError(getString(R.string.badly_formatted_email));
            return;
        } else {
            binding.textInputLayoutEmail.setErrorEnabled(false);
        }

        if (!isPasswordMatchAndNotEmpty(password, confirmPassword)) {
            return;
        }

        hideProgressBar(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Update user name.
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        assert user != null;
                        user.updateProfile(profileUpdates).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()){
                                goToHomeActivity(user);
                            }
                        });

                    } else {
                        hideProgressBar(false);
                        Toast.makeText(this, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    private boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordMatchAndNotEmpty(CharSequence password, CharSequence confirmPassword) {
        if (TextUtils.isEmpty(password)) {
            binding.textInputLayoutPassword.setError(getString(R.string.password_text_box_error));
            return false;
        } else {
            binding.textInputLayoutPassword.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.textInputLayoutConfirmPassword.setError(getString(R.string.password_text_box_error));
            return false;
        } else {
            binding.textInputLayoutConfirmPassword.setErrorEnabled(false);
        }

        // Check if the password mach and it's length greater than 6.
        if (password.equals(confirmPassword)) {
            if (password.length() >= 6) {
                return true;
            } else {
                binding.textInputLayoutPassword.setError(getString(R.string.short_password));
                return false;
            }
        } else {
            binding.textInputLayoutConfirmPassword.setError(getString(R.string.password_not_match));
            return false;
        }
    }

    private void goToHomeActivity(FirebaseUser user) {
        if (user == null) {
            return;
        }
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        finish();
    }

    private void hideProgressBar(boolean hide) {
        if (hide){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}