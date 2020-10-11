package com.itaycohen.toptapper.ui.views;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itaycohen.toptapper.R;

public class CountingView extends RelativeLayout {

    private TextView oneNum;
    private TextView twoNum;
    private TextView threeNum;
    private TextView goText;

    private static final Long ANIM_DURATION = 800L;
    private static final Long SMALL_PAUSE_DURATION = 400L;

    private int xCenter;
    private int yCenter;

    private Listener mListener;

    public CountingView(Context context) {
        super(context);
        initView();
    }

    public CountingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CountingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CountingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void startCount() {
        startCount(0L);
    }

    public void startCount(Long delay) {
        Runnable runnable = () -> {
            runHideThreeAnim();
            runShowHideTwoAnim();
        };
        if (delay == 0)
            runnable.run();
        else
            postDelayed(runnable, delay);
    }



    private void runHideThreeAnim() {
        if (threeNum == null)
            return;

        threeNum.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0.0f)
                .translationX(-150f)
                .translationY(120f)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new EmptyAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (threeNum == null)
                            return;

                        threeNum.setVisibility(View.GONE);
                    }
                });
    }

    private void runShowHideTwoAnim() {
        if (twoNum == null)
            return;

        float twoNumToX = xCenter - (twoNum.getWidth()/2f);
        float twoNumToY = yCenter - (twoNum.getHeight()/2f);

        twoNum.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(1.0f)
                .x(twoNumToX)
                .y(twoNumToY)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new EmptyAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (twoNum == null)
                            return;

                        twoNum.postDelayed((Runnable) () -> {
                            if (twoNum != null) {
                                twoNum.animate()
                                        .x(150f)
                                        .y(120f)
                                        .scaleX(0.25f)
                                        .scaleY(0.25f)
                                        .alpha(0f)
                                        .setDuration(ANIM_DURATION)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setListener(new EmptyAnimatorListener() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                if (twoNum == null)
                                                    return;
                                                twoNum.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        }, SMALL_PAUSE_DURATION);

                        postDelayed(() -> runShowHideOneAnim(), SMALL_PAUSE_DURATION);
                    }
                });
    }

    private void runShowHideOneAnim() {
        if (oneNum==null)
            return;

        final float oneNumFromX = oneNum.getX();
        final float oneNumToX = xCenter - (oneNum.getWidth()/2f);
        final float oneNumFromY = oneNum.getY();
        final float oneNumToY = yCenter - (oneNum.getHeight()/2f);
        oneNum.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(1f)
                .x(oneNumToX)
                .y(oneNumToY)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new EmptyAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (oneNum==null)
                            return;

                        //hide twoNum after small pause
                        oneNum.postDelayed(() -> {
                            if (oneNum != null) {
                                oneNum.animate()
                                        .x(oneNumFromX)
                                        .y(oneNumFromY)
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .alpha(0f)
                                        .setDuration(ANIM_DURATION)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setListener(new EmptyAnimatorListener() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                if (oneNum==null)
                                                    return;
                                                oneNum.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        }, SMALL_PAUSE_DURATION);

                        postDelayed(() -> runGoAnim(), SMALL_PAUSE_DURATION);
                    }
                });
    }

    private void runGoAnim() {
        if (goText == null)
            return;

        float goTextToX = xCenter - (goText.getWidth()/2f);
        float goTextToY = yCenter - (goText.getHeight()/2f);
        goText.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .x(goTextToX)
                .y(goTextToY)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new EmptyAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (goText == null)
                            return;
                        goText.animate()
                                .alpha(0f)
                                .setDuration(ANIM_DURATION)
                                .setListener(new EmptyAnimatorListener() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        if (mListener != null)
                                            mListener.onCountingDone();
                                    }
                                });
                    }
                });
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.counting_view_layout, this, true);
        oneNum = (TextView)findViewById(R.id.oneNum);
        twoNum = (TextView)findViewById(R.id.twoNum);
        threeNum = (TextView)findViewById(R.id.threeNum);
        goText = (TextView)findViewById(R.id.goText);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                xCenter = getWidth()/2;
                yCenter = getHeight()/2;
            }
        });
    }

    private interface EmptyAnimatorListener extends Animator.AnimatorListener{

        @Override
        default void onAnimationStart(Animator animation) {

        }

        @Override
        default void onAnimationEnd(Animator animation) {

        }

        @Override
        default void onAnimationCancel(Animator animation) {

        }

        @Override
        default void onAnimationRepeat(Animator animation) {

        }
    }

    public interface Listener {
        void onCountingDone();
    }
}
