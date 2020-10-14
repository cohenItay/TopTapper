package com.itaycohen.toptapper.ui;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.models.Level;
import com.itaycohen.toptapper.ui.utils.DistinctColorSpan;
import com.itaycohen.toptapper.ui.utils.Utils;
import com.itaycohen.toptapper.ui.views.PlateView;
import com.itaycohen.toptapper.ui.views.CountingView;
import com.itaycohen.toptapper.ui.views.FieldLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

public class TapFragment extends Fragment {

    private CountingView mCountingView;
    private PlateView mBtnsBar;
    private FieldLayout mFieldLayout;
    private ConstraintLayout mRootLayout;
    private TextView mColorsTextView;
    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());
    private @DrawableRes List<Integer> mTotalShapesList;
    private @DrawableRes List<Integer> mPlateShapes;
    private CountingViewListenerImpl mCountingViewListener = new CountingViewListenerImpl();
    private int clickCount = 0;
    private long period = 0;
    private Level level;
    private @ColorRes List<Integer> colorsPool;


    public TapFragment() {
        super(R.layout.fragment_tap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountingView = view.findViewById(R.id.countingView);
        mBtnsBar = view.findViewById(R.id.btnsBar);
        mFieldLayout = view.findViewById(R.id.fieldLayout);
        mRootLayout = view.findViewById(R.id.rootLayout);
        mColorsTextView = view.findViewById(R.id.colorsTextView);
        setup();
        mCountingView.setListener(mCountingViewListener);
    }

    private void setup() {
        mTotalShapesList = Utils.getListOfResourcesFor(getResources(), R.array.total_shapes);
        if (mTotalShapesList.size() < 3)
            throw new IllegalArgumentException("Must be bigger than 3");

        int plateAmount = 0;
        int maxFieldItemsPerCycle = 0;


        level = getArguments() != null ? TapFragmentArgs.fromBundle(getArguments()).getLevel() : null;
        if (level == null)
            throw new IllegalArgumentException("Level must be supplied");

        switch (level) {
            case BASIC:
                period = 1400L;
                plateAmount = 3;
                maxFieldItemsPerCycle = 4;
                break;
            case INTERMIDATE:
                period = 1100L;
                plateAmount = 4;
                maxFieldItemsPerCycle = 5;
                break;
            case EXPERT:
                period = 900L;
                plateAmount = 4;
                maxFieldItemsPerCycle = 6;
                colorsPool = Utils.getListOfResourcesFor(getResources(), R.array.distinct_colors);
                SpannableStringBuilder b = new SpannableStringBuilder(getString(R.string.colors));
                DistinctColorSpan distinctColorSpan = new DistinctColorSpan();
                b.setSpan(distinctColorSpan, 0, b.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mColorsTextView.setText(b);
                break;
        }

        mFieldLayout.setMaxItemsPerCycle(maxFieldItemsPerCycle);
        mPlateShapes = Utils.getRandomItemsFromCollection(plateAmount, mTotalShapesList);
        List<Integer> colorsRes = Utils.getListOfResourcesFor(getResources(), R.array.distinct_colors);
        colorsRes = Utils.getRandomItemsFromCollection(plateAmount, colorsRes);
        mBtnsBar.setShapesArr(mPlateShapes, colorsRes);
    }

    private  class CountingViewListenerImpl implements CountingView.Listener {

        @Override
        public void onCountingDone() {
            if (getView() == null || !getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED))
                return;

            mFieldLayout.setListener(onFieldClick);
            mCountingView.setVisibility(View.INVISIBLE);
            if (period != 0) {
                mFieldLayout.setRespondToTouch(true);
                timer.scheduleAtFixedRate(new RefreshImagesTimerTask(), 0L, period);
            }
        }
    }

    private class RefreshImagesTimerTask extends TimerTask {

        private boolean isCurrentStateColor = false;
        private int periodsForColorChange = 15;
        private int periodIndex = 1;

        @Override
        public void run() {
            handler.post(() -> {
                if (level == Level.EXPERT) {
                    if (periodIndex % periodsForColorChange == 0) {
                        isCurrentStateColor = !isCurrentStateColor;
                        setColorsMode(isCurrentStateColor);
                        periodIndex = 1;
                    }
                    periodIndex++;
                }
                mFieldLayout.refreshImages();
            });
        }
    }

    private void setColorsMode(boolean isColorMode) {
        int resolvedColor = ResourcesCompat.getColor(getResources(), R.color.black_800_a35, requireContext().getTheme());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRootLayout.setForegroundTintList(isColorMode ? ColorStateList.valueOf(resolvedColor) : null);
        }
        mColorsTextView.setVisibility(isColorMode ? View.VISIBLE : View.INVISIBLE);
        mFieldLayout.setColorsPool(isColorMode ? colorsPool : null);
    }

    private FieldLayout.Listener onFieldClick = shapeRes -> {
        if (!mPlateShapes.contains(shapeRes)) {
            timer.cancel();
            mFieldLayout.setRespondToTouch(false);
            showEndSessionDialog();
        } else {
            clickCount++;
        }
    };

    private void showEndSessionDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setTitle(R.string.disqualify)
                .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_banned, requireContext().getTheme()))
                .setMessage(R.string.sesion_end)
                .setPositiveButton(R.string.try_again, (dialog, which) -> {
                    clickCount = 0;
                    timer = new Timer();
                    mCountingView.setVisibility(View.VISIBLE);
                    mFieldLayout.cleanField();
                    setup();
                    mCountingView.startCount(1000L);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                    Navigation.findNavController(requireView()).popBackStack();
                })
                .show();
    }

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
