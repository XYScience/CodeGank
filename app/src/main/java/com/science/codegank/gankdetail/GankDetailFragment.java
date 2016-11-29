package com.science.codegank.gankdetail;

import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.homeday.HomeFragment;
import com.science.codegank.view.MySwipeRefreshLayout;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/24
 */

public class GankDetailFragment extends BaseFragment implements GankDetailContract.View {

    @BindView(R.id.content_webView)
    public WebView mWebView;
    @BindView(R.id.videoContainer)
    public FrameLayout mVideoWebView;
    private GankDetailContract.Presenter mGankDetailPresenter;

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

    }


    public boolean inCustomView() {
        return mGankDetailPresenter.inCustomView();
    }

    public void hideCustomView() {
        mGankDetailPresenter.hideCustomView();
    }
}
