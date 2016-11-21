package com.science.codegank.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.science.codegank.R;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class RatioImageView extends ImageView {

    private int originalWidth;
    private int originalHeight;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        originalWidth = typedArray.getInteger(R.styleable.RatioImageView_ratio_width, 0);
        originalHeight = typedArray.getInteger(R.styleable.RatioImageView_ratio_height, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalHeight > 0 && originalWidth > 0) {
            float radio = (float) originalWidth / (float) originalHeight;

            int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
            int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

            if (measureWidth > 0) {
                measureHeight = (int) ((float) measureWidth / radio);
            }
            setMeasuredDimension(measureWidth, measureHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
