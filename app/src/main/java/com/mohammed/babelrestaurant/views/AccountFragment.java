package com.mohammed.babelrestaurant.views;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.utils.ChangeLocaleLanguageHelper;
import com.mohammed.babelrestaurant.utils.Constants;
import com.mohammed.babelrestaurant.utils.SharedPreferencesHelper;
import com.mohammed.babelrestaurant.databinding.FragmentAccountBinding;
import com.mohammed.babelrestaurant.screen_intro.IntroActivity;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        // Initialise date user.
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Navigate to Orders history fragment
        binding.historyCardView.setOnClickListener(view -> {
            NavDirections action = AccountFragmentDirections.actionAccountFragmentToOrdersHistoryFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        //Navigate to Address Fragment
        binding.addressCardView.setOnClickListener(view -> {
            NavDirections action = AccountFragmentDirections.actionAccountFragmentToAddressFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        ///Sing out.
        binding.logoutCardView.setOnClickListener(view -> {
            if (mAuth != null) {
                alertDialogSignOut();
            }
        });

        // Change language preferences
        binding.languageImageView.setOnClickListener(view -> selectLanguageDialog());


        // Set dark mode preferences.
        binding.darkModeRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) ->
                onRadioButtonClicked(radioGroup));


        //Reset LanguageTextView and RadioButtons according to preferences.
        resetLanguageTextViewAndRadioButtons();
    }


    //Sing out methode.
    private void alertDialogSignOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.are_you_sure);
        dialog.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), IntroActivity.class));
            requireActivity().finish();
        });
        dialog.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

    //Reset LanguageTextView and RadioButtons according to preferences.
    private void resetLanguageTextViewAndRadioButtons() {
        String language = SharedPreferencesHelper.getLanguagePreferences(Constants.LANGUAGE_KEY, getContext());
        String darkMode = SharedPreferencesHelper.getDarkModePreferences(Constants.DARK_MODE_KEY, getContext());

        if (language.equals(Constants.ARABIC)) {
            binding.languageTextView.setText(R.string.arabic);
        } else {
            binding.languageTextView.setText(R.string.english);
        }

        switch (darkMode) {
            case Constants.LIGHT_MODE: {
                binding.radioButtonLight.setChecked(true);
            }
            break;
            case Constants.DARK_MODE: {
                binding.radioButtonDark.setChecked(true);
            }
            break;
            default: {
                binding.radioButtonAuto.setChecked(true);
            }
        }
    }

    // Set dark mode preferences.
    public void onRadioButtonClicked(RadioGroup radioGroup) {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        int buttonId = radioGroup.getCheckedRadioButtonId();

        if (buttonId == R.id.radioButtonLight) {
            // If the light mode is on, no need to apply it again
            if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) return;

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferencesHelper.setDarkModePreferences(Constants.DARK_MODE_KEY, Constants.LIGHT_MODE, getContext());

        } else if (buttonId == R.id.radioButtonDark) {
            // If the nightMode is on, no need to apply it again.
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) return;

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferencesHelper.setDarkModePreferences(Constants.DARK_MODE_KEY, Constants.DARK_MODE, getContext());

        } else {
            // If the nightMode already follow system, no need to apply it as well.
            if (nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) return;

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            SharedPreferencesHelper.setDarkModePreferences(Constants.DARK_MODE_KEY, Constants.FOLLOW_SYSTEM_MODE, getContext());
        }

        // Recreate the activity for the theme change to take effect.
        ActivityCompat.recreate(requireActivity());
    }

    // Change language preferences
    private void selectLanguageDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.select_language);
        dialog.setItems(R.array.language_entries, (dialogInterface, item) -> {
            if (item == 0) {
                ChangeLocaleLanguageHelper.setLocaleLanguage(getContext(), Constants.ARABIC);
            } else {
                ChangeLocaleLanguageHelper.setLocaleLanguage(getContext(), Constants.ENGLISH);
            }
            // Recreate the activity for the language change to take effect.
            ActivityCompat.recreate(requireActivity());
        });
        dialog.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (user != null) {
            binding.userName.setText(user.getDisplayName());
            binding.emailTextView.setText(user.getEmail());
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null){
                Glide.with(binding.accountImageView).load(user.getPhotoUrl()).into(binding.accountImageView);
            } else {
                Glide.with(binding.accountImageView).load(R.drawable.ic_model).into(binding.accountImageView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}