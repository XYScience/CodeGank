package com.science.codegank.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.science.codegank.widget.ScrollStateChangedListener.ScrollState;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public class MyWebView extends WebView implements NestedScrollingChild {
    private static final int INVALID_POINTER = -1;
    private static String TAG = MyWebView.class.getSimpleName();
    private int consumedY;
    private int contentHeight;
    private float density;
    public int direction;
    private DirectionDetector directionDetector;
    private boolean hasTouchWebView;
    private boolean isPageFinished;
    private boolean isScrollUp;
    private OnLongClickListener longClickListenerFalse;
    private OnLongClickListener longClickListenerTrue;
    private int mActivePointerId;
    private NestedScrollingChildHelper mChildHelper;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private OnScrollChangeListener onScrollChangeListener;
    private int originHeight;
    private ViewGroup parentView;
    public ScrollState position;
    int preContentHeight;
    private float preY;
    private ScrollStateChangedListener scrollStateChangedListener;
    private WebSettings settings;
    private int webviewHeight;

    public interface OnScrollChangeListener {
        void onScrollChanged(int i, int i2, int i3, int i4, ScrollState scrollState);
    }

    private class JSGetContentHeight {
        private JSGetContentHeight() {
        }

        @JavascriptInterface
        public void getContentHeight(int i) {
            setContentHeight((int) (((float) i) * density));
        }
    }

    public static class MyWebViewClient extends WebViewClient {
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }

        public boolean shouldHandleUrlLoading(WebView webView, String str) {
            return false;
        }

        @Deprecated
        public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (webView instanceof MyWebView) {
                if (TextUtils.isEmpty(str) || URLUtil.isAboutUrl(str)) {
                    return true;
                } else if (!((MyWebView) webView).isBeingDragged()) {
                    return shouldHandleUrlLoading(webView, str);
                }
            }
            return super.shouldOverrideUrlLoading(webView, str);
        }
    }

    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MyWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScrollConsumed = new int[2];
        this.mScrollOffset = new int[2];
        this.direction = 0;
        this.position = ScrollState.TOP;
        this.preContentHeight = -1;
        this.contentHeight = -1;
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        this.webviewHeight = -1;
        this.isScrollUp = true;
        this.hasTouchWebView = false;
        this.isPageFinished = false;
        init();
    }

    private void endTouch() {
        setJavaScriptEnable(true);
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        recycleVelocityTracker();
        stopNestedScroll();
    }

    private void flingWithNestedDispatch(int i) {
        if (!dispatchNestedPreFling(0.0f, (float) i)) {
            dispatchNestedFling(0.0f, (float) i, true);
        }
    }

    private void getEmbeddedParent(View view) {
        ViewParent parent = view.getParent();
        if (parent == null) {
            return;
        }
        if (parent instanceof ScrollStateChangedListener) {
            this.parentView = (ViewGroup) parent;
            setScrollStateChangedListener((ScrollStateChangedListener) parent);
        } else if (parent instanceof ViewGroup) {
            getEmbeddedParent((ViewGroup) parent);
        }
    }

    private void init() {
        this.mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.directionDetector = new DirectionDetector();
        this.density = getScale();
        setOverScrollMode(2);
        this.settings = getSettings();
        addJavascriptInterface(new JSGetContentHeight(), "InjectedObject");
    }

    private void setJavaScriptEnable(boolean z) {
        if (this.settings.getJavaScriptEnabled() != z) {
            this.settings.setJavaScriptEnabled(z);
        }
    }

    private void setScrollStateChangedListener(ScrollStateChangedListener scrollStateChangedListener) {
        this.scrollStateChangedListener = scrollStateChangedListener;
    }

    public void computeScroll() {
        if (this.position == ScrollState.MIDDLE) {
            super.computeScroll();
        }
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mChildHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mChildHelper.dispatchNestedPreFling(f, f2);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.mChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    public int getWebContentHeight() {
        loadUrl("javascript:window.InjectedObject.getContentHeight(document.getElementsByTagName('html')[0].scrollHeight)");
        return this.contentHeight;
    }

    public boolean hasNestedScrollingParent() {
        return this.mChildHelper.hasNestedScrollingParent();
    }

    public boolean isBeingDragged() {
        return this.mIsBeingDragged;
    }

    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.mChildHelper.setNestedScrollingEnabled(z);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getEmbeddedParent(this);
    }

    private void setLongClickEnable(boolean z) {
        if (z) {
            if (!isLongClickable()) {
                super.setOnLongClickListener(this.longClickListenerFalse);
                setLongClickable(true);
                setHapticFeedbackEnabled(true);
                return;
            }
            return;
        }
        if (this.longClickListenerTrue == null) {
            this.longClickListenerTrue = new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return true;
                }
            };
        }
        super.setOnLongClickListener(this.longClickListenerTrue);
        setLongClickable(false);
        setHapticFeedbackEnabled(false);
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        this.consumedY = i2 - i4;
        if (i2 <= 0) {
            this.position = ScrollState.TOP;
            return;
        }
        if (this.scrollStateChangedListener != null) {
            this.scrollStateChangedListener.onChildPositionChange(this.position);
        }
        if (this.onScrollChangeListener != null) {
            this.onScrollChangeListener.onScrollChanged(i, i2, i3, i4, this.position);
        }
        if (this.consumedY > 0) {
            this.isScrollUp = true;
            if (ViewCompat.canScrollVertically((View) this, 1)) {
                this.position = ScrollState.MIDDLE;
            } else if (this.webviewHeight + i2 >= this.contentHeight - 10) {
                this.position = ScrollState.BOTTOM;
            } else {
                this.position = ScrollState.MIDDLE;
            }
        } else {
            this.isScrollUp = false;
            this.position = ScrollState.MIDDLE;
        }
        if (i2 - i4 < -5) {
            // Show Toolbar
            if (mToolbarListener != null) {
                mToolbarListener.showOrHideToolbar(true);
            }
        } else if (i2 - i4 > 5) {
            // Hide Toolbar
            if (mToolbarListener != null) {
                mToolbarListener.showOrHideToolbar(false);
            }
        }
    }

    public ToolbarListener mToolbarListener;

    public void setToolbarListener(ToolbarListener toolbarListener) {
        mToolbarListener = toolbarListener;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.webviewHeight = i2 + 1;
        if (this.contentHeight < 1) {
            setContentHeight(this.webviewHeight);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.isPageFinished) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        this.hasTouchWebView = true;
        if (this.position == ScrollState.MIDDLE) {
            switch (motionEvent.getAction()) {
                case 0:
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                    startNestedScroll(2);
                    break;
                case 1:
                case 3:
                    endTouch();
                    break;
            }
            if (Build.VERSION.SDK_INT < 21) {
                requestDisallowInterceptTouchEvent(true);
            }
            super.onTouchEvent(motionEvent);
            return true;
        }
        if (Build.VERSION.SDK_INT < 21) {
            if (this.position == ScrollState.BOTTOM && this.isScrollUp) {
                requestDisallowInterceptTouchEvent(true);
            } else if (this.position == ScrollState.TOP) {
                requestDisallowInterceptTouchEvent(true);
            } else {
                requestDisallowInterceptTouchEvent(false);
            }
        }
        int a = MotionEventCompat.getActionMasked(motionEvent);
        initVelocityTrackerIfNotExists();
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        int b = MotionEventCompat.getActionIndex(motionEvent);
        if (a == 0) {
            this.mNestedYOffset = 0;
        }
        obtain.offsetLocation(0.0f, (float) this.mNestedYOffset);
        this.consumedY = 0;
        this.direction = 0;
        switch (a) {
            case 0:
                boolean onTouchEvent = super.onTouchEvent(motionEvent);
                this.mLastMotionY = (int) (motionEvent.getY() + 0.5f);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                this.preY = obtain.getY();
                this.mIsBeingDragged = false;
                startNestedScroll(2);
                z = onTouchEvent;
                break;
            case 1:
                z = super.onTouchEvent(motionEvent);
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                        a = (int) VelocityTrackerCompat.getYVelocity(velocityTracker, this.mActivePointerId);
                        if (Math.abs(a) > this.mMinimumVelocity) {
                            flingWithNestedDispatch(-a);
                        }
                    }
                }
                this.mActivePointerId = -1;
                endTouch();
                break;
            case 2:
                a = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                if (a != -1) {
                    if (this.mIsBeingDragged || Math.abs(obtain.getY() - this.preY) <= ((float) this.mTouchSlop)) {
                    } else {
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                    }
                    b = (int) (MotionEventCompat.getY(motionEvent, a) + 0.5f);
                    a = this.mLastMotionY - b;
                    if (a != 0) {
                        this.direction = this.directionDetector.getDirection(a, true, this.scrollStateChangedListener);
                    }
                    if (dispatchNestedPreScroll(0, a, this.mScrollConsumed, this.mScrollOffset)) {
                        a -= this.mScrollConsumed[1];
                        obtain.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                        this.mNestedYOffset += this.mScrollOffset[1];
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionY = b - this.mScrollOffset[1];
                        int i = a - this.consumedY;
                        boolean onTouchEvent2 = super.onTouchEvent(motionEvent);
                        if (this.position != ScrollState.MIDDLE) {
                            switch (this.direction) {
                                case 1:
                                    if (this.position != ScrollState.BOTTOM && this.contentHeight != this.webviewHeight) {
                                        requestDisallowInterceptTouchEvent(false);
                                        scrollBy(0, i);
                                        break;
                                    }
                                    if (dispatchNestedScroll(0, this.consumedY, 0, i, this.mScrollOffset)) {
                                        obtain.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                                        this.mNestedYOffset += this.mScrollOffset[1];
                                        this.mLastMotionY -= this.mScrollOffset[1];
                                        break;
                                    }
                                    break;
                                case 2:
                                    if (this.position != ScrollState.TOP && this.contentHeight != this.webviewHeight) {
                                        scrollBy(0, i);
                                        break;
                                    }
                                    if (dispatchNestedScroll(0, this.consumedY, 0, i, this.mScrollOffset)) {
                                        obtain.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                                        this.mNestedYOffset += this.mScrollOffset[1];
                                        this.mLastMotionY -= this.mScrollOffset[1];
                                        break;
                                    }
                                    break;
                            }
                            z = onTouchEvent2;
                            break;
                        }
                        return true;
                    }
                }
                break;
            case 3:
                z = super.onTouchEvent(motionEvent);
                break;
            case 5:
                z = super.onTouchEvent(motionEvent);
                this.mLastMotionY = (int) (MotionEventCompat.getY(motionEvent, b) + 0.5f);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, b);
                break;
            case 6:
                z = super.onTouchEvent(motionEvent);
                onSecondaryPointerUp(motionEvent);
                try {
                    this.mLastMotionY = (int) (MotionEventCompat.getY(motionEvent,
                            MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId)) + 0.5f);
                    break;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    break;
                }
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(obtain);
        }
        obtain.recycle();
        return z;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (MotionEventCompat.getPointerId(motionEvent, action) == this.mActivePointerId) {
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, action == 0 ? 1 : 0);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (z) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(z);
    }

    protected boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        int i9;
        if (this.position != ScrollState.MIDDLE) {
            i9 = 0;
        } else {
            i9 = i2;
        }
        return super.overScrollBy(i, i9, i3, i4, i5, i6, i7, i8, z);
    }

    public void scrollToBottom() {
        scrollTo(getScrollX(), this.contentHeight - this.webviewHeight);
    }

    public void scrollToTop() {
        scrollTo(getScrollX(), 0);
    }

    public void setContentHeight(int i) {
        this.contentHeight = i;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.longClickListenerFalse = onLongClickListener;
        super.setOnLongClickListener(onLongClickListener);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        if (webViewClient instanceof WebViewClient) {
            super.setWebViewClient(webViewClient);
            return;
        }
        throw new IllegalArgumentException("WebViewClient should be instance of EmbeddedWebView$WebViewClient");
    }

    public boolean startNestedScroll(int i) {
        return this.mChildHelper.startNestedScroll(i);
    }

    protected void onDetachedFromWindow() {
        this.mChildHelper.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public void stopNestedScroll() {
        this.mChildHelper.stopNestedScroll();
    }

    public void setPageFinished(boolean z) {
        this.isPageFinished = z;
    }

    public boolean hasTouchWebView() {
        return this.hasTouchWebView;
    }
}
