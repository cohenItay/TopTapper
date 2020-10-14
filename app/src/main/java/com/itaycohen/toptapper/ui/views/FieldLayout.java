package com.itaycohen.toptapper.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class FieldLayout extends GridLayout {

    private int mItemWidth;
    private int mItemHeight;
    @DrawableRes
    private int[] mShapesResArr;
    private int mMaxItemsPerCycle;
    private ImageView[][] imageViewsMatrix;
    private Listener mListener;
    private boolean mRespondToTouch = true;
    @ColorRes
    private List<Integer> colorsPool = Arrays.asList(R.color.black_900);


    public FieldLayout(@NonNull Context context) {
        super(context);
    }

    public FieldLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        extractAttrs(attrs);
    }

    public FieldLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractAttrs(attrs);
    }

    public FieldLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractAttrs(attrs);
    }

    public void setRespondToTouch(boolean mResponsToTouch) {
        this.mRespondToTouch = mResponsToTouch;
    }

    public void setMaxItemsPerCycle(int mItemsPerCycle) {
        if (mItemsPerCycle > mShapesResArr.length)
            return;

        this.mMaxItemsPerCycle = mItemsPerCycle;
    }

    public void setColorsPool(@ColorRes List<Integer> colorsPool) {
        this.colorsPool = (colorsPool != null && colorsPool.size() > 0) ? colorsPool : Arrays.asList(R.color.black_900);
    }

    public void refreshImages() {
        if (!isParamsValid())
            return;

        cleanField();
        Random random = new Random(); // 0 -> mMaxItemsPerCycle-1
        int shapesAmount = random.nextInt(mMaxItemsPerCycle)+1;
        for (int shapeRes : getRandomShapesResArr(shapesAmount)) {
            int r = random.nextInt(getColumnCount());
            int c = random.nextInt(getRowCount());
            ImageView iv = imageViewsMatrix[r][c];
            iv.setImageResource(shapeRes);
            int randomColorRes = colorsPool.get(random.nextInt(colorsPool.size()));
            int resolvedColor = ResourcesCompat.getColor(getResources(), randomColorRes, getContext().getTheme());
            iv.setColorFilter(resolvedColor);
            iv.setTag(shapeRes);
        }
    }

    public void cleanField() {
        for(int i=0; i<getChildCount(); i++) {
            cleanImage((ImageView)getChildAt(i));
        }
    }

    public void setListener(Listener mListener) {
        this.mListener = mListener;
        setOnTouchListener((v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    // disqualify user when press on bg
                    if (mListener != null && mRespondToTouch)
                        mListener.onShapeDownTouch(-1);
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.performClick();
            }
            return false;
        });
    }



    private boolean isParamsValid() {
        return mItemHeight > 10 &&
                mItemWidth > 10 &&
                mShapesResArr.length > 0 &&
                mMaxItemsPerCycle > 0;
    }

    private void extractAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FieldLayout);
        mItemHeight = typedArray.getDimensionPixelSize(R.styleable.FieldLayout_item_layout_height, 0);
        mItemWidth = typedArray.getDimensionPixelSize(R.styleable.FieldLayout_item_layout_width, 0);
        int resId = typedArray.getResourceId(R.styleable.FieldLayout_shapes_pool_arr, 0);
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
        removeAllViews();
        updateGrid(w, h);

    }

    private void updateGrid(int width, int height) {
        if (mItemHeight == 0 || mItemWidth == 0)
            return;

        int widthAmount = (width - getPaddingStart() - getPaddingEnd())/mItemWidth;
        int heightAmount = (height - getPaddingStart() - getPaddingEnd())/mItemHeight;

        setColumnCount(widthAmount);
        setRowCount(heightAmount);
        populateImageViews();
    }

    private void populateImageViews() {
        int cCount = getColumnCount();
        int rCount = getRowCount();
        imageViewsMatrix = new ImageView[cCount][rCount];
        for (int c=0; c<cCount; c++) {
            for (int r=0; r<rCount; r++) {
                ImageView imageView = createImageView(new Pair<>(c, r));
                imageViewsMatrix[c][r] = imageView;
                addView(imageView);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private ImageView createImageView(Pair<Integer, Integer> gridLocation) {
        ImageView imageView = new ImageView(getContext());
        Spec columnSpec = GridLayout.spec(gridLocation.first, 1);
        Spec rowSpec = GridLayout.spec(gridLocation.second, 1);
        LayoutParams params = new LayoutParams(rowSpec, columnSpec);
        params.setGravity(Gravity.CENTER);
        imageView.setLayoutParams(params);
        imageView.setOnTouchListener((v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    // disqualify user when press on bg
                    if (mListener != null && mRespondToTouch) {
                        Object tag = imageView.getTag();
                        int shapeRes = tag != null ? (int) tag : -1;
                        mListener.onShapeDownTouch(shapeRes);
                        cleanImage(imageView);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    v.performClick();
                    return true;
                }
            }
            return false;
        });
        return imageView;
    }

    private void cleanImage(ImageView child) {
        child.setImageResource(0);
        child.setTag(null);
    }

    private int[] getRandomShapesResArr(int forAmount) {
        List<Integer> shapesResList = new ArrayList<>(mShapesResArr.length);
        for (int shapeRes : mShapesResArr)
            shapesResList.add(shapeRes);

        List<Integer> randomShapesList = Utils.getRandomItemsFromCollection(forAmount, shapesResList);

        int[] randomShapes = new int[forAmount];
        for (int i=0; i<forAmount; i++)
            randomShapes[i] = randomShapesList.get(i);
        return randomShapes;
    }

    public interface Listener {
        void onShapeDownTouch(@DrawableRes int shapeRes);
    }
}
