package com.mohammed.babelrestaurant.screen_intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.databinding.FragmentSlideOneBinding;
import com.mohammed.babelrestaurant.auth.GoogleSignInActivity;

public class SlideOneFragment extends Fragment {
    private FragmentSlideOneBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideOneBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nextButton.setOnClickListener(view1 ->
                Navigation.findNavController(view1).
                        navigate(R.id.action_slideOneFragment_to_slideTowFragment));

        binding.skipTextView.setOnClickListener(view12 -> {
            startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
            requireActivity().finish();
        });

    }
}