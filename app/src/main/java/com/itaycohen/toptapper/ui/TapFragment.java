package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.models.Level;
import com.itaycohen.toptapper.ui.views.ButtonsBar;
import com.itaycohen.toptapper.ui.views.CountingView;
import com.itaycohen.toptapper.ui.views.FieldLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TapFragment extends Fragment {

    public static final String KEY_LEVEL = "KLEVEL";
    private CountingView mCountingView;
    private ButtonsBar mBtnsBar;
    private FieldLayout mFieldLayout;
    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());
    private @DrawableRes List<Integer> mTotalShapesList;
    private @DrawableRes List<Integer> mPlateShapes;
    private CountingViewListenerImpl mCountingViewListener = new CountingViewListenerImpl();

    public TapFragment() {
        super(R.layout.fragment_tap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountingView = view.findViewById(R.id.countingView);
        mBtnsBar = view.findViewById(R.id.btnsBar);
        mFieldLayout = view.findViewById(R.id.fieldLayout);
        mFieldLayout.setListener(onFieldClick);
        setup();
        mCountingView.setListener(mCountingViewListener);
    }

    private void setup() {
        mTotalShapesList = Utils.getListOfResourcesFor(getResources(), R.array.total_shapes);
        if (mTotalShapesList.size() < 3)
            throw new IllegalArgumentException("Must be bigger than 3");

        int plateAmount = 0;
        int maxFieldItemsPerCycle = 0;
        boolean useColors = false;
        Level level = null;
        if (getArguments() != null)
            level = (Level)getArguments().getSerializable(KEY_LEVEL);
        if (level == null)
            return;

        switch (level) {
            case BASIC:
                mCountingViewListener.setPeriod(1400L);
                plateAmount = 3;
                maxFieldItemsPerCycle = 4;
                break;
            case INTERMIDATE:
                mCountingViewListener.setPeriod(900L);
                plateAmount = 4;
                maxFieldItemsPerCycle = 5;
                break;
            case EXPERT:
                mCountingViewListener.setPeriod(900L);
                plateAmount = 4;
                maxFieldItemsPerCycle = 6;
                useColors = true;
                break;
        }

        mFieldLayout.setMaxItemsPerCycle(maxFieldItemsPerCycle);
        mBtnsBar.setShapesArr(Utils.getRandomItemsFromCollection(plateAmount, mTotalShapesList));
    }

    private  class CountingViewListenerImpl implements CountingView.Listener {

        private long period = 0;

        public void setPeriod(long period) {
            this.period = period;
        }

        @Override
        public void onCountingDone() {
            if (getView() == null)
                return;

            ((ViewGroup)getView()).removeView(mCountingView);
            if (period != 0)
                timer.scheduleAtFixedRate(timerTask, 0L, 0L);
        }
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(() -> mFieldLayout.refreshImages());
        }
    };

    private FieldLayout.Listener onFieldClick = shapeRes -> {
        if (!mPlateShapes.contains(shapeRes))
            timer.cancel();
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
