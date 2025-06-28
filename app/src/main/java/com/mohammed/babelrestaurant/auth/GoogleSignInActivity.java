package com.mohammed.babelrestaurant.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mohammed.babelrestaurant.views.HomeActivity;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.databinding.ActivityGoogleSignInBinding;

public class GoogleSignInActivity extends AppCompatActivity {
    private ActivityGoogleSignInBinding binding;
    private static final String TAG = "GoogleActivity";
    private static final int REQ_SIGN_IN = 800;
    private static final int REQ_ONE_TAP = 900;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private OneTapSignIn mOneTapSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Initialise OneTapSigning class.
        mOneTapSignIn = new OneTapSignIn(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.googleSignInButton.setOnClickListener(view -> signInWithGoogle());

        binding.registerButton.setOnClickListener(view -> signInWithEmail());

        binding.newAccountButton.setOnClickListener(view ->
                startActivity(new Intent(this,SignUpActivity.class)));

        binding.forgetPasswordTextView.setOnClickListener(view ->
                startActivity(new Intent(this, ForgetPasswordActivity.class)));


        // Show one tap UI if no users registered.
        if (mAuth.getCurrentUser() == null) {
            mOneTapSignIn.showOneTapUiSignIn();
        }
    }

    // Sign in with Google account.
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_SIGN_IN);
    }

    // Sign in with your email account.
    private void signInWithEmail() {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.textBoxNote),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        hideProgressBar(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        goToHomeActivity(user);
                    } else {
                        hideProgressBar(false);
                        Toast.makeText(this, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // If th request was from Google SignIn One Tap UI.
            case REQ_ONE_TAP:
                try {
                    SignInCredential googleCredential = mOneTapSignIn.getOneTapClient().getSignInCredentialFromIntent(data);
                    String idToken = googleCredential.getGoogleIdToken();
                    if (idToken != null) {
                        // Got an ID token from Google. Authenticate with Firebase.
                        firebaseAuthWithGoogle(idToken);
                    }
                } catch (ApiException e) {
                    if (e.getStatusCode() == CommonStatusCodes.CANCELED) {
                        Toast.makeText(this, "One-tap dialog was closed."
                                , Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                // If the request was from the regular sign in with Google
            case REQ_SIGN_IN:
                try {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    // Got an ID token from Google. Authenticate with Firebase.
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Log.d(TAG, "" + e.getLocalizedMessage());
                    Toast.makeText(this, "SigIn failed " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        goToHomeActivity(user);
                    } else {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        goToHomeActivity(user);
    }

    private void goToHomeActivity(FirebaseUser user) {
        if (user == null) {
            return;
        }
        startActivity(new Intent(GoogleSignInActivity.this, HomeActivity.class));
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