package com.science.codegank.widget.nestedscrollwebview;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.science.codegank.widget.DirectionDetector;
import com.science.codegank.widget.ScrollStateChangedListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public class NestedScrollView extends ScrollView
        implements NestedScrollingParent, ScrollStateChangedListener {
    private static int SENSOR_DISTANCE = 0;
    private static final String TAG = "EmbeddedScrollView";
    private ScrollState childPosition = ScrollState.TOP;
    private int currentSwapLine = -1;
    private int direction = 0;
    private boolean firstInitSize = true;
    boolean hasNestedScroll = false;
    private boolean isTouchUp;
    private float lastY;
    private NestedScrollingParentHelper mParentHelper;
    private List<View> scrollingChildList;
    private int touchSlop;

    public NestedScrollView(Context paramContext) {
        this(paramContext, null);
    }

    public NestedScrollView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void analyNestedScrollingChildViews() {
        View localView1 = getChildAt(0);
        if ((localView1 == null) || (!(localView1 instanceof ViewGroup)))
            throw new IllegalArgumentException("EmbeddedScrollView root child illegal");
        this.scrollingChildList = new ArrayList();
        ViewGroup localViewGroup = (ViewGroup) localView1;
        for (int i = 0; i < localViewGroup.getChildCount(); i++) {
            View localView2 = localViewGroup.getChildAt(i);
            if ((localView2 instanceof NestedScrollingChild))
                this.scrollingChildList.add(localView2);
        }
    }

    private void consumeEvent(int dx, int dy, int[] consumed) {
        scrollBy(dx, dy);
        consumed[0] = 0;
        consumed[1] = dy;
        Log.i(TAG, "parent consumed pre : " + consumed[1]);
    }

    private void init() {
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.mParentHelper = new NestedScrollingParentHelper(this);
        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        SENSOR_DISTANCE = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0F, getResources().getDisplayMetrics());
        Log.i(TAG, "touch slop = " + this.touchSlop + "  SENSOR_DISTANCE = " + SENSOR_DISTANCE);
    }

    private void setApproachLine(int mScrollY) {
        switch (this.direction) {
            case 1: {
                int temp = -1;
                Iterator childIterator = this.scrollingChildList.iterator();
                while (childIterator.hasNext()) {
                    int viewTop = ((View) childIterator.next()).getTop();
                    if (mScrollY <= viewTop) {
                        int distance = viewTop - mScrollY;
                        if ((temp == -1) || (distance < temp)) {
                            temp = distance;
                            setCurrentSwapLine(viewTop);
                            return;
                        }
                    }
                }
                setCurrentSwapLine(-1);
            }
            break;
            case 2: {
                int i = -1;
                Iterator childIterator = this.scrollingChildList.iterator();
                while (childIterator.hasNext()) {
                    int viewTop = ((View) childIterator.next()).getTop();
                    if (mScrollY >= viewTop) {
                        int k = mScrollY - viewTop;
                        if ((i == -1) || (k < i)) {
                            i = k;
                            setCurrentSwapLine(viewTop);
                            return;
                        }
                    }
                }
                setCurrentSwapLine(-1);
            }
            break;
        }
    }

    private void setCurrentSwapLine(int currentSwapLine) {
        this.currentSwapLine = currentSwapLine;
        Log.i(TAG, "currentSwapLine = " + currentSwapLine);
    }

    private void setNestedScrollViewHeight() {
        Iterator childIterator = this.scrollingChildList.iterator();
        while (childIterator.hasNext()) {
            View localView = (View) childIterator.next();
            ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
            if ((localLayoutParams.height == -1) || (localLayoutParams.height == -2)) {
                int i = getMeasuredHeight();
                localLayoutParams.height = i;
                localView.setLayoutParams(localLayoutParams);
                Log.i(TAG, "setNestedScrollViewHeight = " + i + "  view = " + localView);
            }
        }
    }

    private void setTouchState(MotionEvent ev) {
        this.isTouchUp = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                stopScrolling();
                this.isTouchUp = false;
                this.lastY = ev.getY();
                this.direction = 0;
            }
            break;
            case MotionEvent.ACTION_MOVE:
                float f = ev.getY();
                int i = (int) (this.lastY - f);
                if (i != 0) {
                    this.direction = DirectionDetector.getDirection(i, true);
                }
                this.lastY = f;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.isTouchUp = true;
                break;
        }
    }

    private void stopScrolling() {
        smoothScrollBy(0, 0);
    }

    private void stopScrolling(int x, int y) {
        smoothScrollTo(x, y);
    }

    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    public void onChildDirectionChange(int direction) {
        this.direction = direction;
        Log.i(TAG, "onChildDirectionChange = " + direction);
    }

    public void onChildPositionChange(ScrollState parama) {
        this.childPosition = parama;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        analyNestedScrollingChildViews();
    }

//  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
//    stopScrolling();
//    boolean bool = super.onInterceptTouchEvent(paramMotionEvent);
//    if (this.hasNestedScroll) {
//      bool = false;
//    }
//    setTouchState(paramMotionEvent);
//    return bool;
//  }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i(TAG, "onLayout : " + t + " / " + b);
        if (this.firstInitSize) {
            setNestedScrollViewHeight();
            setCurrentSwapLine(((View) this.scrollingChildList.get(0)).getTop());
            this.firstInitSize = false;
        }
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG, "onNestedFling : velocityY = " + velocityY + " " + consumed);
        if (!consumed) {
            fling((int) velocityY);
            return true;
        }
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i(TAG, "onNestedPreFling :  direction = " + this.direction + " currentSwapLine = " + this.currentSwapLine + " velocityY = " + velocityY + " scrollY = " + getScrollY());
        int scrollY = getScrollY();
        if (scrollY < this.currentSwapLine) {
            Log.i(TAG, "fling 111111=="+velocityY);
            fling((int) velocityY);
            return true;
        }
        if (scrollY > this.currentSwapLine) {
            Log.i(TAG, "fling 222222");
            fling((int) velocityY);
            return true;
        }
        return false;
    }


    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int scrollY = getScrollY();
        Log.i(TAG, "onNestedPreScroll : direction = " + this.direction + "  currentSwapLine = " + this.currentSwapLine + "  dy = " + dy + "  scrollY = " + scrollY);
        if (scrollY < this.currentSwapLine) {
            Log.i(TAG, "consumeEvent 111111");
            if ((this.direction == 1) && (this.currentSwapLine != -1) && (scrollY + dy > this.currentSwapLine)) {
                dy = this.currentSwapLine - scrollY;
            }
            consumeEvent(dx, dy, consumed);
        }
        if(scrollY <= this.currentSwapLine)
            return;
        Log.i(TAG, "consumeEvent 222222");
        if ((this.direction == 2) && (this.currentSwapLine != -1) && (scrollY + dy < this.currentSwapLine)) {
            dy = this.currentSwapLine - scrollY;
        }
        consumeEvent(dx, dy, consumed);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(TAG, "parent consumed : dyConsumed = " + dxConsumed + " , dyUnconsumed = " + dyUnconsumed);
        scrollBy(dxConsumed, dyUnconsumed);
//    super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.hasNestedScroll = true;
        this.mParentHelper.onNestedScrollAccepted(child, target, axes);
        Log.i(TAG, "============== start nested scroll ===============");
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.i(TAG, "onOverScrolled : " + scrollY + " " + clampedY + " isTouchUp = " + this.isTouchUp + "/" + this.hasNestedScroll + " currentSwapLine = " + this.currentSwapLine);
        if (((this.isTouchUp) || (!this.hasNestedScroll)) && (this.currentSwapLine != -1)) {
            int i = scrollY - this.currentSwapLine;
            Log.i(TAG,"IIIIIIIIIIII====="+i);
            Log.i(TAG,"SENSOR_DISTANCE==="+SENSOR_DISTANCE);
            if (Math.abs(i) < SENSOR_DISTANCE) {
                if ((this.direction == 1) && (i > 0)) {
                    Log.i(TAG, "1111scroll to currentSwapLine = " + this.currentSwapLine);
                    stopScrolling(0, this.currentSwapLine);
                    return;
                }
                if ((this.direction == 2) && (i < 0)) {
                    Log.i(TAG, "2222scroll to currentSwapLine = " + this.currentSwapLine);
                    stopScrolling(0, this.currentSwapLine);
                    return;
                }
            }
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    protected void onScrollChanged(int mScrollX, int mScrollY, int oldX, int oldY) {
        super.onScrollChanged(mScrollX, mScrollY, oldX, oldY);
        this.direction = DirectionDetector.getDirection(mScrollY - oldY, true);
        Log.i(TAG, "onScrollChanged : top = " + mScrollY + " oldY = " + oldY + "  direction = " + this.direction);
        setApproachLine(mScrollY);
    }

    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        Log.i(TAG, "onSizeChanged : " + paramInt2 + "/" + paramInt4);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & SCROLL_AXIS_VERTICAL) != 0;
    }

    public void onStopNestedScroll(View target) {
        Log.i(TAG, "onStopNestedScroll : " + target);
        this.hasNestedScroll = false;
        this.mParentHelper.onStopNestedScroll(target);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        setTouchState(paramMotionEvent);
        return super.onTouchEvent(paramMotionEvent);
    }
}
