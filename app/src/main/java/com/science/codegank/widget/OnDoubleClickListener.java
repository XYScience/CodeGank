package com.science.codegank.widget;

import android.view.View;

/**
 * @author SScience
 * @description 实现双击事件
 * @email chentushen.science@gmail.com
 * @data 2016/11/11
 */

public abstract class OnDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 300;
    private long mLastTime = 0;
    private long mCurTime = 0;

    @Override
    public void onClick(View v) {
        mLastTime = mCurTime;
        mCurTime = System.currentTimeMillis();
        if (mCurTime - mLastTime < MIN_CLICK_DELAY_TIME) {//双击事件
            mCurTime = 0;
            mLastTime = 0;
            onClicks(v);
        } else {
        }
    }

    public abstract void onClicks(View v);
}
