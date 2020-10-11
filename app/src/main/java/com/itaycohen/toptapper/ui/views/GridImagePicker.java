package com.itaycohen.toptapper.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.itaycohen.toptapper.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class GridImagePicker extends ConstraintLayout {

    private @DrawableRes int[] drawablesArr;
    private ImageView[] imagesArr = new ImageView[6];
    private Listener mListener;

    public GridImagePicker(@NonNull Context context) {
        super(context);
        init();
    }

    public GridImagePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        extractAttrs(attrs);
        init();
    }

    public GridImagePicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractAttrs(attrs);
        init();
    }

    public GridImagePicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractAttrs(attrs);
        init();
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }


    private void extractAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GridImagePicker);
        int arrayResourceId = typedArray.getResourceId(R.styleable.GridImagePicker_six_images, 0);
        if (arrayResourceId != 0) {
            final TypedArray resourceArray = getResources().obtainTypedArray(arrayResourceId);
            if (resourceArray.length() > 0) {
                drawablesArr = new int[6];
                for (int i = 0; i < resourceArray.length(); i++) {
                    final int resourceId = resourceArray.getResourceId(i, 0);
                    drawablesArr[i] = resourceId;
                }
                resourceArray.recycle();
            }
        }
        typedArray.recycle();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_avatar_picker, this, true);
        imagesArr[0] = findViewById(R.id.imageView);
        imagesArr[1] = findViewById(R.id.imageView2);
        imagesArr[2] = findViewById(R.id.imageView3);
        imagesArr[3] = findViewById(R.id.imageView4);
        imagesArr[4] = findViewById(R.id.imageView5);
        imagesArr[5] = findViewById(R.id.imageView6);

        for (int i=0; i<imagesArr.length; i++ ) {
            ImageView imageView = imagesArr[i];
            int finalI = i;
            if (drawablesArr != null)
                imageView.setBackgroundResource(drawablesArr[i]);
            imageView.setOnClickListener(v -> {
                for (ImageView iv : imagesArr) {
                    iv.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                }
                imageView.setImageTintList(
                        ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.purple_200, getContext().getTheme()))
                );
                if (mListener != null)
                    mListener.onImageChecked(drawablesArr[finalI]);
            });
        }
    }



    public interface Listener {
        void onImageChecked(@DrawableRes int drawableRes);
    }
}
