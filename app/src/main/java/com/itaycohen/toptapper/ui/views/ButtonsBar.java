package com.itaycohen.toptapper.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.ui.Utils;

import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class ButtonsBar extends LinearLayout {

    private int[] colorsArr;
    private int[] shapesArr;

    public ButtonsBar(Context context) {
        super(context);
        initView();
    }

    public ButtonsBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        extractAttrs(attrs);
        initView();
    }

    public ButtonsBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractAttrs(attrs);
        initView();
    }

    public ButtonsBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractAttrs(attrs);
        initView();
    }



    public void setShapesArr(@DrawableRes List<Integer> shapesArr) {
        setShapesArr(Utils.listToArray(shapesArr));
    }

    public void setShapesArr(@DrawableRes int[] shapesArr) {
        setShapesArr(shapesArr, null);
    }

    public void setShapesArr(@DrawableRes List<Integer> shapesArr, @ColorRes List<Integer> colorsArr) {
        setShapesArr(Utils.listToArray(shapesArr), Utils.listToArray(colorsArr));
    }
    public void setShapesArr(@DrawableRes int[] shapesArr, @ColorRes int[] colorsArr) {
        this.shapesArr = shapesArr;
        if (colorsArr == null || colorsArr.length == shapesArr.length)
            this.colorsArr = colorsArr;
        updateBar();
    }

    private void extractAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonsBar);
        int arrayShapeResourceId = typedArray.getResourceId(R.styleable.ButtonsBar_buttons_bar_shapes, 0);
        int arrayColorsResourceId = typedArray.getResourceId(R.styleable.ButtonsBar_buttons_bar_colors, 0);
        if (arrayShapeResourceId != 0) {
            final TypedArray resourceShapesArray = getResources().obtainTypedArray(arrayShapeResourceId);
            final TypedArray resourceColorsArray = getResources().obtainTypedArray(arrayColorsResourceId);
            if (resourceShapesArray.length() > 0) {
                shapesArr = new int[resourceShapesArray.length()];
                colorsArr = new int[resourceShapesArray.length()];
                for (int i = 0; i < resourceShapesArray.length(); i++) {
                    shapesArr[i] = resourceShapesArray.getResourceId(i, 0);
                    colorsArr[i] = resourceColorsArray.getResourceId(i, 0);
                }
                resourceShapesArray.recycle();
                resourceColorsArray.recycle();
            }
        }
        typedArray.recycle();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        updateBar();
    }

    private void updateBar() {
        removeAllViews();
        for (int i=0; i<shapesArr.length; i++) {
            ImageButton button = new ImageButton(getContext());
            button.setId(View.generateViewId());
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            params.setMarginEnd((int)getResources().getDimension(R.dimen.button_bar_margin));
            button.setLayoutParams(params);
            button.setImageResource(shapesArr[i]);
            button.setImageTintList(ResourcesCompat.getColorStateList(getResources(), colorsArr[i], getContext().getTheme()));
            button.setBackgroundResource(android.R.color.transparent);
            int finalI = i;
            addView(button);
        }
    }
}
