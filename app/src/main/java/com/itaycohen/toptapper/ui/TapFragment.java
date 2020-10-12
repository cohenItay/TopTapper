package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.view.View;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.views.ButtonsBar;
import com.itaycohen.toptapper.ui.views.CountingView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TapFragment extends Fragment {

    private CountingView mCountingView;
    private ButtonsBar mBtnsBar;

    public TapFragment() {
        super(R.layout.fragment_tap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCountingView = view.findViewById(R.id.countingView);
        mBtnsBar = view.findViewById(R.id.btnsBar);
        mCountingView.setListener(() -> {
            if (mCountingView != null)
                mCountingView.setVisibility(View.GONE);

        });
        mBtnsBar.setListener((btn, position, shapeRes, colorRes) -> {

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCountingView.startCount(1000L);
    }
}
