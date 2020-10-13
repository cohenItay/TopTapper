package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.views.ButtonsBar;
import com.itaycohen.toptapper.ui.views.CountingView;
import com.itaycohen.toptapper.ui.views.FieldLayout;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TapFragment extends Fragment {

    private CountingView mCountingView;
    private ButtonsBar mBtnsBar;
    private FieldLayout mFieldLayout;
    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());

    public TapFragment() {
        super(R.layout.fragment_tap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCountingView = view.findViewById(R.id.countingView);
        mBtnsBar = view.findViewById(R.id.btnsBar);
        mFieldLayout = view.findViewById(R.id.fieldLayout);
        mCountingView.setListener(() -> {
            if (getView() == null)
                return;

            ((ViewGroup)getView()).removeView(mCountingView);
            timer.scheduleAtFixedRate(timerTask, 0L, 800L);
        });
        mFieldLayout.setMaxItemsPerCycle(5);
        mFieldLayout.setListener(shapeRes -> {
            Log.d("tagg", "shapeRes: "+ shapeRes);
        });
        getView().setOnClickListener(v -> timer.cancel());
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(() -> mFieldLayout.refreshImages());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mCountingView.startCount(1000L);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }
}
