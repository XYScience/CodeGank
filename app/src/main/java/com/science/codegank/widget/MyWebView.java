package com.science.codegank.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public class MyWebView extends WebView {

    private MyWebView mMyWebView;
    private OnScrollChangedCallback mOnScrollChangedCallback;
    private static final int HIDE_THRESHOLD = 26;
    private int mScrolledDistance = 0;
    private boolean mControlsVisible = true;

    public MyWebView(final Context context) {
        super(context);
    }

    public MyWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(final Context context, final AttributeSet attrs,
                     final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mMyWebView.getScrollY() == 0) {
            if (!mControlsVisible) {
                mOnScrollChangedCallback.onScroll(true);
                mControlsVisible = true;
            }
        } else {
            if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                mOnScrollChangedCallback.onScroll(false);
                mControlsVisible = false;
                mScrolledDistance = 0;
            } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                mOnScrollChangedCallback.onScroll(true);
                mControlsVisible = true;
                mScrolledDistance = 0;
            }
        }

        if ((mControlsVisible && t - oldt > 0) || (!mControlsVisible && t - oldt < 0)) {
            mScrolledDistance += (t - oldt);
        }
    }

    public void setOnScrollChangedCallback(MyWebView webView, OnScrollChangedCallback onScrollChangedCallback) {
        mMyWebView = webView;
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        void onScroll(Boolean isScrollDown);
    }
}
