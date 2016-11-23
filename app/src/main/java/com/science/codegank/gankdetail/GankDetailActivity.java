package com.science.codegank.gankdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.homeday.HomeFragment;

import butterknife.BindView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/24
 */

public class GankDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content_webView)
    WebView mContentWebView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gank_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = setToolbar(getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_DESC));
        mContentWebView.loadUrl(getIntent().getStringExtra(HomeFragment.EXTRA_BUNDLE_URL));
    }
}
