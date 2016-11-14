package com.science.codegank.welfaredetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.homeday.HomeFragment;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/14
 */

public class WelfareDetailActivity extends BaseActivity {

    private Toolbar mToolbar;
    private View mViewStatusBar;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_welfare_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mViewStatusBar = CommonUtil.setStatusBarColor(this, R.color.translucentBg);
        mToolbar = setToolbar("");

        ImageView ivWelfare = (ImageView) findViewById(R.id.iv_welfare);
        ImageLoadUtil.loadImageIfCrop(this, getIntent().getStringExtra(HomeFragment.WELFARE_URL), 0, false, ivWelfare);
        ivWelfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFade();
            }
        });
    }

    private void toggleFade() {
        if (mToolbar.getVisibility() == View.VISIBLE) {
            CommonUtil.animateOut(mToolbar, R.anim.viewer_toolbar_fade_out);
            CommonUtil.animateOut(mViewStatusBar, R.anim.viewer_toolbar_fade_out);
            CommonUtil.setSystemUiVisible(this);
        } else {
            CommonUtil.animateIn(mToolbar, R.anim.viewer_toolbar_fade_in);
            CommonUtil.animateIn(mViewStatusBar, R.anim.viewer_toolbar_fade_in);
            CommonUtil.setSystemUiInVisible(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_welfare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                break;
            case R.id.menu_save_img:
                break;
            case R.id.menu_save_as_wallpaper:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
