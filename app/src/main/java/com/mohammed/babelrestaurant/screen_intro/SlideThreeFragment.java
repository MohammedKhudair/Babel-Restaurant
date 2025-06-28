package com.mohammed.babelrestaurant.screen_intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mohammed.babelrestaurant.databinding.FragmentSlideThreeBinding;
import com.mohammed.babelrestaurant.auth.GoogleSignInActivity;


public class SlideThreeFragment extends Fragment {
    private FragmentSlideThreeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideThreeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.doneButton.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
            requireActivity().finish();
        });

        binding.skipTextView.setOnClickListener(view12 -> {
            startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
            requireActivity().finish();
        });

    }
}