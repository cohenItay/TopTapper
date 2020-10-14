package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.view.View;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.models.Level;

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
            proceed(Level.BASIC);
        });
        view.findViewById(R.id.avgBtn).setOnClickListener(v -> {
            proceed(Level.INTERMIDATE);
        });
        view.findViewById(R.id.expertBtn).setOnClickListener(v -> {
            proceed(Level.EXPERT);
        });
    }

    private void proceed(Level level) {
        Bundle b = new Bundle();
        b.putSerializable(TapFragment.KEY_LEVEL, level);
        Navigation.findNavController(getView()).navigate(R.id.action_levelFragment_to_tapFragment);
    }
}
