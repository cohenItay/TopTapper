package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.view.View;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.repos.UserRepository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class EntranceFragment extends Fragment {

    public static final String CONTINUE_TO_LEVELS_KEY = "CONSDTOASKDF";

    public EntranceFragment() {
        super(R.layout.fragment_entrance);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.play_btn).setOnClickListener(v -> {
                    UserRepository userRepo = UserRepository.getInstance();
                    if (userRepo.getUserModel() == null) {
                        Bundle b = new Bundle();
                        b.putBoolean(CONTINUE_TO_LEVELS_KEY, true);
                        Navigation.findNavController(v).navigate(R.id.action_entranceFragment_to_defineUserFragment, b);
                    } else {
                        Navigation.findNavController(v).navigate(R.id.action_entranceFragment_to_levelFragment);
                    }
                }
        );

        view.findViewById(R.id.changeUserBtn).setOnClickListener (v -> {
                    Bundle b = new Bundle();
                    b.putBoolean("continue_to_levels", false);
                    Navigation.findNavController(v).navigate(R.id.action_entranceFragment_to_defineUserFragment, b);
                }
        );
    }
}
