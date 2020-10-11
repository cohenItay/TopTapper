package com.itaycohen.toptapper.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.itaycohen.toptapper.R;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

public class ButtonsBar extends LinearLayout {

    private int[] colorsArr;
    private ImageButton[] buttonsArr = new ImageButton[4];
    private Listener mListener;

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

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    private void extractAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonsBar);
        int arrayResourceId = typedArray.getResourceId(R.styleable.ButtonsBar_buttons_bar_colors, 0);
        if (arrayResourceId != 0) {
            final TypedArray resourceArray = getResources().obtainTypedArray(arrayResourceId);
            if (resourceArray.length() > 0) {
                colorsArr = new int[resourceArray.length()];
                for (int i = 0; i < resourceArray.length(); i++) {
                    final int resourceId = resourceArray.getResourceId(i, 0);
                    colorsArr[i] = resourceId;
                }
                resourceArray.recycle();
            }
        }
        typedArray.recycle();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0; i<colorsArr.length; i++) {
            ImageButton button = new ImageButton(getContext());
            button.setId(View.generateViewId());
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            params.setMarginEnd((int)getResources().getDimension(R.dimen.button_bar_margin));
            button.setLayoutParams(params);
            button.setBackgroundResource(colorsArr[i]);
            int finalI = i;
            button.setOnClickListener(v -> mListener.onButtonClick((Button)v, finalI, colorsArr[finalI]));
            addView(button);
        }
    }

    public interface Listener {
        void onButtonClick(Button btn, int position, @IdRes int color);
    }
}
