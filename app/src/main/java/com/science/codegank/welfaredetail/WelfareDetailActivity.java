package com.science.codegank.welfaredetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/14
 */

public class WelfareDetailActivity extends BaseActivity {

    private static final String EXTRA_BUNDLE_URL = "bundle_url";
    private static final String EXTRA_BUNDLE_TITLE = "bundle_title";
    private static final String TRANSTION_NAME_IMAGE = "transition_name_img";
    private static final String TRANSTION_NAME_TITLE = "transition_name_title";
    private Toolbar mToolbar;
    private View mViewStatusBar;

    public static void intentWelfareDetail(Activity activity, String url, String title, View img, View text) {
        Intent intent = new Intent(activity, WelfareDetailActivity.class);
        intent.putExtra(EXTRA_BUNDLE_URL, url);
        intent.putExtra(EXTRA_BUNDLE_TITLE, title);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                new Pair<View, String>(img, TRANSTION_NAME_IMAGE),
                new Pair<View, String>(text, TRANSTION_NAME_TITLE)
        );
        ActivityCompat.startActivity(activity, intent, activityOptionsCompat.toBundle());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_welfare_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        mViewStatusBar = CommonUtil.setStatusBarColor(this, R.color.translucentBg);
        mToolbar = setToolbar("");

        ImageView ivWelfare = (ImageView) findViewById(R.id.iv_welfare);
        TextView tvDate = (TextView) findViewById(R.id.toolbar_title);
        tvDate.setText(getIntent().getStringExtra(EXTRA_BUNDLE_TITLE));
        ViewCompat.setTransitionName(tvDate, TRANSTION_NAME_TITLE);
        ViewCompat.setTransitionName(ivWelfare, TRANSTION_NAME_IMAGE);

        ImageLoadUtil.loadImageIfCrop(this, getIntent().getStringExtra(EXTRA_BUNDLE_URL), 0, false, ivWelfare);
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
