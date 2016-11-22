package com.science.codegank.welfaredetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.science.codegank.R;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/17
 */

public class WelfareDetailActivity extends AppCompatActivity implements WelfareDetailContract.View {

    private static final String EXTRA_BUNDLE_URL = "bundle_url";
    private static final String EXTRA_BUNDLE_TITLE = "bundle_title";
    @BindView(R.id.iv_welfare)
    ImageView mIvWelfare;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private View mViewStatusBar;
    private WelfareDetailContract.Presenter welfareDetailPresenter;

    public static void intentWelfareDetail(Activity activity, String url, String title, View img) {
        Intent intent = new Intent(activity, WelfareDetailActivity.class);
        intent.putExtra(EXTRA_BUNDLE_URL, url);
        intent.putExtra(EXTRA_BUNDLE_TITLE, title);
        activity.startActivity(intent);

//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                activity, img, activity.getString(R.string.transition_name_welfare));
//        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mViewStatusBar = CommonUtil.setStatusBarColor(this, R.color.translucentBg);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbarTitle.setText(getIntent().getStringExtra(EXTRA_BUNDLE_TITLE));
        ImageLoadUtil.loadImage(this, getIntent().getStringExtra(EXTRA_BUNDLE_URL), 0, mIvWelfare);
        mIvWelfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFade();
            }
        });

        welfareDetailPresenter = new WelfareDetailPresenter(this, this);
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
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_share:
                welfareDetailPresenter.shareWelfare();
                break;
            case R.id.menu_save_img:
                welfareDetailPresenter.saveWelfare(getIntent().getStringExtra(EXTRA_BUNDLE_URL));
                break;
            case R.id.menu_save_as_wallpaper:
                welfareDetailPresenter.setWelfareToWallpaper();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveWelfareSuccess() {

    }

    @Override
    public void setWelfareToWallpaperSuccess() {

    }
}
