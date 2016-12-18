package com.science.codegank.module.gankdetail;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.module.homeday.HomeFragment;
import com.science.codegank.widget.MySwipeRefreshLayout;
import com.science.codegank.widget.MyWebView;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/24
 */

public class GankDetailFragment extends BaseFragment implements GankDetailContract.View, MyWebView.OnScrollChangedCallback {

    @BindView(R.id.content_webView)
    public MyWebView mWebView;
    @BindView(R.id.videoContainer)
    public FrameLayout mVideoWebView;
    @BindView(R.id.btn_collect)
    FloatingActionButton mBtnCollect;
    private GankDetailContract.Presenter mGankDetailPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_gank_detail;
    }

    @Override
    protected void doCreateView(View view) {
        MySwipeRefreshLayout swipeRefreshLayout = (MySwipeRefreshLayout) initRefreshLayout(view);
        swipeRefreshLayout.setScrollUpChild(mWebView);
        int start = (int) (getActivity().getResources().getDisplayMetrics().density * 20.0f);
        int end = (int) (getActivity().getResources().getDisplayMetrics().density * 56.0f) + start;
        swipeRefreshLayout.setProgressViewOffset(true, start, end);
        mGankDetailPresenter.setUpWebView(mWebView, mVideoWebView);
        mGankDetailPresenter.loadUrl(mWebView, getActivity().getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_URL));

        mWebView.setOnScrollChangedCallback(mWebView, this);
        mBtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
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
        AppBarLayout appbarLayout = ((GankDetailActivity) getActivity()).mAppbarLayout;
        if (isScrollDown) {
            appbarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            mBtnCollect.show();
        } else {
            appbarLayout.animate().translationY(-appbarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            mBtnCollect.hide();
        }
    }
}
