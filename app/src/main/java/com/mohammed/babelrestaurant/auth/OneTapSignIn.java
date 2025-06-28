package com.mohammed.babelrestaurant.auth;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.mohammed.babelrestaurant.R;

public class OneTapSignIn {
    private static final String TAG = "GoogleActivity";
    private static final int REQ_ONE_TAP = 900;
    private final Context context;

    private BeginSignInRequest signInRequest;
    private BeginSignInRequest signUpRequest;
    private final SignInClient oneTapClient;


    public OneTapSignIn(Context context) {
        this.context = context;
        oneTapClient = Identity.getSignInClient(context);
        // We call sigInR and signUpR fun to initialise variables.
        signUpRequest();
        signInRequest();
    }

    private void signUpRequest() {
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(context.getString(R.string.default_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
    }

    private void signInRequest() {
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(context.getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(false)
                .build();
    }

    // Show OneTabUi for registered users.
    public void showOneTapUiSignIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener((Activity) context, result -> {
                    try {
                        ((Activity) context).startIntentSenderForResult(result.getPendingIntent().getIntentSender(),
                                REQ_ONE_TAP, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        showToastMessage("Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener((Activity) context, e -> {
                    Log.d(TAG, e.getLocalizedMessage());
                    showToastMessage("No google accounts registered.");
                    // No accounts registered before, so we call showOneTapUiSigUp for the new accounts.
                    showOneTapUiSigUp();
                });
    }

    //  Show OneTabUi for new users that haven't registered before.
    private void showOneTapUiSigUp() {
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener((Activity) context, result -> {
                    try {
                        ((Activity) context).startIntentSenderForResult(result.getPendingIntent().getIntentSender(),
                                REQ_ONE_TAP, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        showToastMessage("Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener((Activity) context, e -> {
                    Log.d(TAG, e.getLocalizedMessage());
                    showToastMessage("No Google Accounts found 😧.");
                });
    }

    private void showToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public SignInClient getOneTapClient() {
        return oneTapClient;
    }

}
