package com.science.codegank.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.science.codegank.R;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/9/25
 */

public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription mCompositeSubscription;
    protected boolean isVisible;
    private boolean isFirst = false;

    protected abstract int getContentLayout();

    protected abstract void doCreateView(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayout(), container, false);
        ButterKnife.bind(this, view);
        doCreateView(view);
        isFirst = true;
        return view;
    }

    /**
     * 创建一个CompositeSubscription对象来进行管理异步处理与Activity生命周期
     *
     * @param subscription
     */
    protected void addCompositeSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    protected SwipeRefreshLayout initRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.swipe_red, R.color.swipe_yellow, R.color.swipe_blue);
        //mSwipeRefreshLayout.setProgressViewOffset(true, -200, 50);
        return mSwipeRefreshLayout;
    }

    protected boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    protected void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    protected void setSwipeRefreshEnable(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    protected boolean getSwipeRefreshLayout() {
        if (mSwipeRefreshLayout != null) {
            return mSwipeRefreshLayout.isEnabled();
        }
        return false;
    }

    @Override
    public void onRefresh() {
    }

    /**
     * viewpager切换时调用，而且是在onCreateView之前调用
     *
     * @param isVisibleToUser true：用户可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    /**
     * 使用add(), hide()，show()添加fragment时
     * 刚开始add()时，当前fragment会调用该方法，但是目标fragment不会调用；
     * 所以先add()所有fragment，即先初始化控件，但不初始化数据。
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    private void onVisible() {
        if (isFirst && isVisible) {
            onLazyLoad();
            isFirst = false; // 控制fragment可见时，是否自动加载数据。
        }
    }

    /**
     * fragment可见时再加载数据
     */
    public abstract void onLazyLoad();

    private void onInVisible() {

    }

}
