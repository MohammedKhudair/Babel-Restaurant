package com.mohammed.babelrestaurant.screen_intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.mohammed.babelrestaurant.databinding.FragmentIntroSlideBinding;


public class IntroSlideFragment extends Fragment {
private FragmentIntroSlideBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIntroSlideBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imageButton.setOnClickListener(view1 -> {
            NavDirections action = IntroSlideFragmentDirections.actionIntroSlideFragmentToSlideOneFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);

        });
    }
}