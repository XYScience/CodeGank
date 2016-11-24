package com.science.codegank.gankdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.homeday.HomeFragment;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/24
 */

public class GankDetailActivity extends BaseActivity {

    private GankDetailFragment mGankDetailFragment;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gank_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_DESC));
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
            mGankDetailFragment.mWebView.destroy();
        }
    }
}
