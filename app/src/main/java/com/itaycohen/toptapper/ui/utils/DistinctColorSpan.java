package com.itaycohen.toptapper.ui.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DistinctColorSpan extends ReplacementSpan {

    private LinearGradient shader;

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        float w = paint.measureText(text, start, end - start);
        int num = end - start;
        int[] colors = new int[num];
        float[] positions = new float[num];
        float cx = 0;
        float[] hsv = {
                0, 1, 1
        };
        for (int i = 0; i < num; i++) {
            float cw = paint.measureText(text, start+i, start+i+1);
            positions[i] = cx / w;
            cx += cw;
            hsv[0] = i * 360f / num;
            colors[i] = Color.HSVToColor(255, hsv);
        }
        shader = new LinearGradient(0, 0, w, 0, colors, positions, Shader.TileMode.CLAMP);
        return (int) w;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setShader(shader);
        canvas.drawText(text, start, end, x, y, paint);
    }
}