package com.science.codegank.module.gankdetail;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.module.homeday.HomeFragment;
import com.science.codegank.widget.MySwipeRefreshLayout;
import com.science.codegank.widget.NestedScrollWebView;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/24
 */

public class GankDetailFragment extends BaseFragment implements GankDetailContract.View, NestedScrollWebView.OnScrollChangedCallback {

    @BindView(R.id.content_webView)
    public NestedScrollWebView mWebView;
    @BindView(R.id.videoContainer)
    public FrameLayout mVideoWebView;
    private GankDetailContract.Presenter mGankDetailPresenter;
    private FloatingActionButton mFabCollect;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_gank_detail;
    }

    @Override
    protected void doCreateView(View view) {
        MySwipeRefreshLayout swipeRefreshLayout = (MySwipeRefreshLayout) initRefreshLayout(view);
        swipeRefreshLayout.setScrollUpChild(mWebView);
        mGankDetailPresenter.setUpWebView(mWebView, mVideoWebView);
        mGankDetailPresenter.loadUrl(mWebView, getActivity().getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_URL));
        mFabCollect = ((GankDetailActivity) getActivity()).mBtnCollect;

        mWebView.setOnScrollChangedCallback(mWebView, this);
    }

    @Override
    public void loadStart() {
        setRefreshing(true);
    }

    @Override
    public void loadWebViewFinished() {
        setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mWebView.reload();
    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void setPresenter(GankDetailContract.Presenter presenter) {
        if (presenter != null) {
            mGankDetailPresenter = presenter;
        }
    }

    public void shareGank() {
        mGankDetailPresenter.shareGank(mWebView.getUrl());
    }

    public void openInBrowser() {
        mGankDetailPresenter.openInBrowser(mWebView.getUrl());
    }

    @Override
    public void getDataError(String msg) {
        setRefreshing(false);
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    public boolean inCustomView() {
        return mGankDetailPresenter.inCustomView();
    }

    public void hideCustomView() {
        mGankDetailPresenter.hideCustomView();
    }

    @Override
    public void onScroll(Boolean isScrollDown) {
        if (isScrollDown) {
            mFabCollect.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        } else {
            mFabCollect.animate().translationY(mFabCollect.getHeight() + getMarginBottom(mFabCollect)).setInterpolator(new AccelerateInterpolator(2));
        }
    }

    private int getMarginBottom(View v) {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }
}
