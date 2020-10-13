package com.itaycohen.toptapper.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.itaycohen.toptapper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FieldView extends FrameLayout {

    private int mItemWidth;
    private int mItemHeight;
    @DrawableRes
    private int[] mShapesResArr;
    private int mMaxItemsPerCycle;
    private int mCycleTime;
    private int[][] mFieldMtrx;

    public FieldView(@NonNull Context context) {
        super(context);
        initView();
    }

    public FieldView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        extractAttrs(attrs);
        initView();
    }

    public FieldView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractAttrs(attrs);
        initView();
    }

    public FieldView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractAttrs(attrs);
        initView();
    }

    public void setMaxItemsPerCycle(int mItemsPerCycle) {
        if (mItemsPerCycle > mShapesResArr.length)
            return;

        this.mMaxItemsPerCycle = mItemsPerCycle;
    }

    public void cycleThrewRandomShapes(int cycleTimeSecs) {
        this.mCycleTime = Math.min(4000, cycleTimeSecs*1000);
        if (isParamsValid()) {
            startCycling();
        }
    }

    private boolean isParamsValid() {
        return mCycleTime > 250 &&
                mItemHeight > 10 &&
                mItemWidth > 10 &&
                mShapesResArr.length > 0 &&
                mMaxItemsPerCycle > 0;
    }

    private void extractAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FieldView);
        mItemHeight = typedArray.getDimensionPixelSize(R.styleable.FieldView_item_layout_height, 0);
        mItemWidth = typedArray.getDimensionPixelSize(R.styleable.FieldView_item_layout_width, 0);
        int resId = typedArray.getResourceId(R.styleable.FieldView_shapes_pool_arr, 0);
        if (resId != 0) {
            typedArray.recycle();
            typedArray = getResources().obtainTypedArray(resId);
            if (typedArray.length() > 0) {
                mShapesResArr = new int[typedArray.length()];
                @DrawableRes int drawRes;
                for (int i=0; i< typedArray.length(); i++) {
                    drawRes = typedArray.getResourceId(i, -1);
                    if (drawRes != -1)
                        mShapesResArr[i] = drawRes;
                }
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateMatrixSize(w, h);
    }

    private void updateMatrixSize(int width, int height) {
        int widthAmount = width/mItemWidth
    }

    private void initView() {

    }

    private void startCycling() {
        removeAllViews();
        post(revealShapesRunnable);
    }

    private Runnable revealShapesRunnable = () -> {
        Random random = new Random(mMaxItemsPerCycle); // 0 -> mMaxItemsPerCycle-1
        int shapesAmount = random.nextInt()+1;
        int[] shapesToShow = getRandomShapesResArr(shapesAmount);
        for (int shapeRes : mShapesResArr) {
            ImageView imageView = createItem(shapeRes);
        }
    };

    private int[] getRandomShapesResArr(int forAmount) {
        List<Integer> shapesResList = new ArrayList<>(mShapesResArr.length);
        for (int shapeRes : mShapesResArr)
            shapesResList.add(shapeRes);
        Collections.shuffle(shapesResList);

        int[] randomShapes = new int[forAmount];
        for (int i=0; i<forAmount; i++)
            randomShapes[i] = shapesResList.get(i);

        return randomShapes;
    }

    private ImageView createItem(int shapeRes) {
        ImageView imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(mItemWidth, mItemHeight);
        imageView.setLayoutParams(params);
        imageView.setImageResource(shapeRes);
    }
}
