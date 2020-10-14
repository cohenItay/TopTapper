package com.itaycohen.toptapper.ui;

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

import java.util.Collection;
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
    private PlateView mPlateView;
    private FieldLayout mFieldLayout;
    private ConstraintLayout mRootLayout;
    private TextView mColorsTextView;
    private View mBlackMask;
    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());
    private @DrawableRes List<Integer> mTotalShapesList;
    private @DrawableRes List<Integer> mPlateShapesRes;
    private @ColorRes List<Integer> mPlateColorsRes;
    private CountingViewListenerImpl mCountingViewListener = new CountingViewListenerImpl();
    private int clickCount = 0;
    private long period = 0;
    private Level level;
    private @ColorRes List<Integer> colorsPool;
    private boolean isCurrentStateColor = false;


    public TapFragment() {
        super(R.layout.fragment_tap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountingView = view.findViewById(R.id.countingView);
        mPlateView = view.findViewById(R.id.btnsBar);
        mFieldLayout = view.findViewById(R.id.fieldLayout);
        mRootLayout = view.findViewById(R.id.rootLayout);
        mColorsTextView = view.findViewById(R.id.colorsTextView);
        mBlackMask = view.findViewById(R.id.blackMask);
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
                period = 1300L;
                plateAmount = 4;
                maxFieldItemsPerCycle = 5;
                break;
            case EXPERT:
                period = 1200L;
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
        mPlateShapesRes = Utils.getRandomItemsFromCollection(plateAmount, mTotalShapesList);
        mPlateColorsRes = Utils.getListOfResourcesFor(getResources(), R.array.distinct_colors);
        mPlateColorsRes = Utils.getRandomItemsFromCollection(plateAmount, mPlateColorsRes);
        mPlateView.setShapesArr(mPlateShapesRes, mPlateColorsRes);
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

        private int periodsForColorChange = 15;
        private int periodIndex = 1;

        @Override
        public void run() {
            handler.post(() -> {
                if (level == Level.EXPERT) {
                    if (periodIndex % periodsForColorChange == 0) {
                        isCurrentStateColor = !isCurrentStateColor;
                        onColorsModeChange(isCurrentStateColor);
                        periodIndex = 1;
                    }
                    periodIndex++;
                }
                mFieldLayout.refreshImages();
            });
        }
    }

    private void onColorsModeChange(boolean isColorMode) {
        mBlackMask.setVisibility(isColorMode ? View.VISIBLE : View.INVISIBLE);
        mColorsTextView.setVisibility(isColorMode ? View.VISIBLE : View.INVISIBLE);
        mFieldLayout.setColorsPool(isColorMode ? colorsPool : null);
    }

    private FieldLayout.Listener onFieldClick = (shapeRes, colorRes) -> {
        Collection<Integer> checkCollection = isCurrentStateColor ? mPlateColorsRes: mPlateShapesRes;
        if (!checkCollection.contains(isCurrentStateColor ? colorRes : shapeRes)) {
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
                    isCurrentStateColor = false;
                    onColorsModeChange(false);
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
