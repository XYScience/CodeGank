package com.science.codegank.module.gankdetail;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.module.homeday.HomeFragment;
import com.science.codegank.widget.MyScrollView;
import com.science.codegank.widget.MySwipeRefreshLayout;
import com.science.codegank.widget.MyWebView;
import com.science.codegank.widget.ToolbarListener;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/24
 */

public class GankDetailFragment extends BaseFragment implements GankDetailContract.View, ToolbarListener {

    @BindView(R.id.content_webView)
    public MyWebView mWebView;
    @BindView(R.id.videoContainer)
    public FrameLayout mVideoWebView;
    @BindView(R.id.scrollView)
    MyScrollView mScrollView;
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

        mWebView.setToolbarListener(this);
        mScrollView.setToolbarListener(this);
    }

    @Override
    public void loadStart() {
        setRefreshing(true);
    }

    @Override
    public void loadWebViewFinished() {
        setRefreshing(false);
        setSwipeRefreshEnable(false);
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
    public void showOrHideToolbar(Boolean state) {
        if (state) {
            animateBack(((GankDetailActivity) getActivity()).mAppbarLayout);
        } else {
            animateHide(((GankDetailActivity) getActivity()).mAppbarLayout);
        }
    }

    // 恢复动画
    private AnimatorSet mAnimatorSetBack;

    private void animateBack(AppBarLayout appBarLayout) {
        // 取消其他动画
        if (mAnimatorSetHide != null && mAnimatorSetHide.isRunning()) {
            mAnimatorSetHide.cancel();
        }
        if (mAnimatorSetBack != null && mAnimatorSetBack.isRunning()) {

        } else {
            mAnimatorSetBack = new AnimatorSet();
            ObjectAnimator animateHeader = ObjectAnimator.ofFloat(appBarLayout, "translationY", appBarLayout.getTranslationY(), 0f);
//            ObjectAnimator animateFooter = ObjectAnimator.ofFloat(mActionButton, "translationY", mActionButton.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(animateHeader);
//            animators.add(animateFooter);
            mAnimatorSetBack.setDuration(300);
            mAnimatorSetBack.playTogether(animators);
            mAnimatorSetBack.start();
        }
    }

    // 隐藏动画
    private AnimatorSet mAnimatorSetHide;

    private void animateHide(AppBarLayout appBarLayout) {
        // 取消其他动画
        if (mAnimatorSetBack != null && mAnimatorSetBack.isRunning()) {
            mAnimatorSetBack.cancel();
        }
        if (mAnimatorSetHide != null && mAnimatorSetHide.isRunning()) {

        } else {
            mAnimatorSetHide = new AnimatorSet();
            ObjectAnimator animateHeader = ObjectAnimator.ofFloat(appBarLayout, "translationY",
                    appBarLayout.getTranslationY(), -appBarLayout.getHeight());
//            ObjectAnimator animateFooter = ObjectAnimator.ofFloat(mActionButton, "translationY",
//                    mActionButton.getTranslationY(), mActionButton.getHeight() + mActionButton.getBottom());
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(animateHeader);
//            animators.add(animateFooter);
            mAnimatorSetHide.setDuration(300);
            mAnimatorSetHide.playTogether(animators);
            mAnimatorSetHide.start();
        }
    }
}
