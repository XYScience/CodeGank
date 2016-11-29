package com.science.codegank.gankdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.homeday.HomeFragment;
import com.science.codegank.util.CommonUtil;

import butterknife.BindView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/24
 */

public class GankDetailActivity extends BaseActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    private GankDetailFragment mGankDetailFragment;
    public Toolbar mToolbar;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gank_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = setToolbar(getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_DESC));
        mGankDetailFragment = new GankDetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_gank_detail, mGankDetailFragment).commit();
        new GankDetailPresenter(this, mGankDetailFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mGankDetailFragment.mWebView.canGoBack()) {
            mGankDetailFragment.mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGankDetailFragment.mWebView != null) {
            mGankDetailFragment.mWebView.loadUrl("about:blank");
            mGankDetailFragment.mWebView.stopLoading();
            mGankDetailFragment.mWebView.setWebChromeClient(null);
            mGankDetailFragment.mWebView.setWebViewClient(null);
            mGankDetailFragment.mWebView.destroy();
            mGankDetailFragment.mWebView = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGankDetailFragment.mWebView != null) {
            mGankDetailFragment.mWebView.onResume();
            mGankDetailFragment.mWebView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGankDetailFragment.mWebView != null) {
            mGankDetailFragment.mWebView.onPause();
            mGankDetailFragment.mWebView.pauseTimers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gank_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_copy_link) {
            CommonUtil.copyToClipBoard(this, getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_URL));
            snackBarShow(mCoordinatorLayout, getString(R.string.copy_success));
        } else if (id == R.id.menu_open_in_browser) {
            mGankDetailFragment.openInBrowser();
        } else if (id == R.id.menu_share) {
            mGankDetailFragment.shareGank();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mGankDetailFragment.inCustomView()) {
            mGankDetailFragment.hideCustomView();
        } else {
            super.onBackPressed();
        }
    }
}
