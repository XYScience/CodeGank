package com.science.codegank.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public class MyScrollView extends ScrollView implements NestedScrollingParent, ScrollStateChangedListener {
    private static int SENSOR_DISTANCE = 0;
    private static final String TAG = "EmbeddedScrollView";
    private ScrollState childPosition;
    private int currentSwapLine;
    private int direction;
    private boolean firstInitSize;
    boolean hasNestedScroll;
    private boolean isPageFinished;
    private boolean isTouchUp;
    private float lastY;
    private NestedScrollingParentHelper mParentHelper;
    private List<View> scrollingChildList;
    private int touchSlop;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.hasNestedScroll = false;
        this.childPosition = ScrollState.TOP;
        this.currentSwapLine = -1;
        this.direction = 0;
        this.firstInitSize = true;
        this.isPageFinished = false;
        init();
    }

    private void analyNestedScrollingChildViews() {
        int i = 0;
        View childAt = getChildAt(0);
        if (childAt == null || !(childAt instanceof ViewGroup)) {
            throw new IllegalArgumentException("EmbeddedScrollView root child illegal");
        }
        this.scrollingChildList = new ArrayList();
        ViewGroup viewGroup = (ViewGroup) childAt;
        while (i < viewGroup.getChildCount()) {
            View childAt2 = viewGroup.getChildAt(i);
            if (childAt2 instanceof NestedScrollingChild) {
                scrollingChildList.add(childAt2);
            }
            i++;
        }
    }

    private void consumeEvent(int i, int i2, int[] iArr) {
        scrollBy(i, i2);
        iArr[0] = 0;
        iArr[1] = i2;
    }

    private void init() {
        setOverScrollMode(2);
        mParentHelper = new NestedScrollingParentHelper(this);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        SENSOR_DISTANCE = (int) TypedValue.applyDimension(1, 100.0f, getResources().getDisplayMetrics());
    }

    private void setApproachLine(int i) {
        int top;
        int i2;
        switch (this.direction) {
            case 1:
                for (View top2 : this.scrollingChildList) {
                    top = top2.getTop();
                    if (i <= top) {
                        i2 = top - i;
                        setCurrentSwapLine(top);
                        return;
                    }
                }
                setCurrentSwapLine(-1);
                return;
            case 2:
                for (View top22 : this.scrollingChildList) {
                    top = top22.getTop();
                    if (i >= top) {
                        i2 = i - top;
                        setCurrentSwapLine(top);
                        return;
                    }
                }
                setCurrentSwapLine(-1);
                return;
            default:
                return;
        }
    }

    private void setCurrentSwapLine(int i) {
        this.currentSwapLine = i;
    }

    private void setNestedScrollViewHeight() {
        for (View view : scrollingChildList) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams.height == -1 || layoutParams.height == -2) {
                int measuredHeight = getMeasuredHeight();
                layoutParams.height = measuredHeight;
                view.setLayoutParams(layoutParams);
            }
        }
    }

    private void setTouchState(MotionEvent motionEvent) {
        this.isTouchUp = false;
        switch (motionEvent.getAction()) {
            case 0:
                stopScrolling();
                this.isTouchUp = false;
                this.lastY = motionEvent.getY();
                this.direction = 0;
                return;
            case 1:
            case 3:
                this.isTouchUp = true;
                return;
            case 2:
                float y = motionEvent.getY();
                int i = (int) (this.lastY - y);
                if (i != 0) {
                    this.direction = DirectionDetector.getDirection(i, true);
                }
                this.lastY = y;
                return;
            default:
                return;
        }
    }

    private void stopScrolling() {
        smoothScrollBy(0, 0);
    }

    private void stopScrolling(int i, int i2) {
        smoothScrollTo(i, i2);
    }

    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    public void onChildDirectionChange(int i) {
        this.direction = i;
    }

    public void onChildPositionChange(ScrollState scrollState) {
        this.childPosition = scrollState;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        analyNestedScrollingChildViews();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.firstInitSize) {
            setNestedScrollViewHeight();
            setCurrentSwapLine(((View) this.scrollingChildList.get(0)).getTop());
            this.firstInitSize = false;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0 || motionEvent.getRawX() > ((getResources().getDisplayMetrics().density * 15.0f) + 0.5f)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        if (z) {
            return false;
        }
        fling((int) f2);
        return true;
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        int scrollY = getScrollY();
        if (scrollY < this.currentSwapLine) {
            fling((int) f2);
            return true;
        } else if (scrollY <= this.currentSwapLine) {
            return false;
        } else {
            fling((int) f2);
            return true;
        }
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        int scrollY = getScrollY();
        if (scrollY < currentSwapLine) {
            if (direction == 1 && currentSwapLine != -1 && scrollY + i2 > currentSwapLine) {
                i2 = currentSwapLine - scrollY;
            }
            consumeEvent(i, i2, iArr);
        } else if (scrollY == currentSwapLine && direction == 1 && currentSwapLine != -1 && !ViewCompat.canScrollVertically(view, 1)) {
            consumeEvent(i, i2, iArr);
        } else if (scrollY > currentSwapLine) {
            if (direction == 2 && currentSwapLine != -1 && scrollY + i2 < currentSwapLine) {
                i2 = currentSwapLine - scrollY;
            }
            consumeEvent(i, i2, iArr);
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        scrollBy(i, i4);
    }

    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.hasNestedScroll = true;
        this.mParentHelper.onNestedScrollAccepted(view, view2, i);
    }

    protected void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        if (this.isPageFinished) {
            if ((this.isTouchUp || !this.hasNestedScroll) && this.currentSwapLine != -1) {
                int i3 = i2 - this.currentSwapLine;
                if (Math.abs(i3) < SENSOR_DISTANCE) {
                    if (this.direction == 1 && i3 > 0) {
                        stopScrolling(0, this.currentSwapLine);
                        return;
                    } else if (this.direction == 2 && i3 < 0) {
                        stopScrolling(0, this.currentSwapLine);
                        return;
                    }
                }
            }
            super.onOverScrolled(i, i2, z, z2);
        }
    }

    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        this.direction = DirectionDetector.getDirection(i2 - i4, true);
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
        setApproachLine(i2);
    }

    public ToolbarListener mToolbarListener;

    public void setToolbarListener(ToolbarListener toolbarListener) {
        mToolbarListener = toolbarListener;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        return (i & 2) != 0;
    }

    public void onStopNestedScroll(View view) {
        this.hasNestedScroll = false;
        this.mParentHelper.onStopNestedScroll(view);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        setTouchState(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void setPageFinished(boolean z) {
        this.isPageFinished = z;
    }
}
