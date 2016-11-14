package com.science.codegank.category.welfare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.util.CommonUtil;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class WelfareListActivity extends BaseActivity {

    private View mStatusBarView, mTranslucentStatusBarView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_welfare;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mStatusBarView = CommonUtil.setStatusBarColor(this, R.color.colorPrimaryDark);
        mTranslucentStatusBarView = CommonUtil.setTranslucentStatusBar(this, R.color.translucentBg);
        mTranslucentStatusBarView.setAlpha(0);
        setToolbar(getString(R.string.welfare));
        WelfareListFragment welfareFragment = new WelfareListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_random, welfareFragment).commit();
        new WelfareListPresenter(welfareFragment);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mStatusBarView, 1 - percentage);
                ViewCompat.setAlpha(mTranslucentStatusBarView, percentage);
            }
        });
    }
}
