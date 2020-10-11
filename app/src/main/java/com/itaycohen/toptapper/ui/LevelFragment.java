package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.view.View;

import com.itaycohen.toptapper.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LevelFragment extends Fragment {

    public LevelFragment() {
        super(R.layout.fragment_level);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.newbBtn).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_levelFragment_to_tapFragment);
        });
    }
}
