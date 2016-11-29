package com.science.codegank.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.widget.TextView;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.util.CommonUtil;

import butterknife.BindView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/29
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar("");

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange() - (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mToolbarTitle, percentage);
            }
        });

        mTvVersion.setText(getString(R.string.version, CommonUtil.getAppVersion(this)));
    }
}
