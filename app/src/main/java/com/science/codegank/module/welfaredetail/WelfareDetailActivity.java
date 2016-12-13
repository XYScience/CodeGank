package com.science.codegank.module.welfaredetail;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.util.CommonDefine;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;
import com.science.codegank.util.SharedPreferenceUtil;

import java.io.File;

import butterknife.BindView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/17
 */

public class WelfareDetailActivity extends BaseActivity implements WelfareDetailContract.View {

    private static final String EXTRA_BUNDLE_URL = "bundle_url";
    private static final String EXTRA_BUNDLE_TITLE = "bundle_title";
    @BindView(R.id.iv_welfare)
    ImageView mIvWelfare;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
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
    protected int getContentLayout() {
        return R.layout.activity_welfare_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mViewStatusBar = CommonUtil.setStatusBarColor(this, R.color.translucentBg);
        super.onCreate(savedInstanceState);
        final Toolbar toolbar = setToolbar("");

        mToolbarTitle.setText(getIntent().getStringExtra(EXTRA_BUNDLE_TITLE));
        if ((Boolean) SharedPreferenceUtil.get(this, CommonDefine.SP_KEY_SMART_NO_PIC, false)) {
            ImageLoadUtil.loadImage(this, R.drawable.welfare, 0, mIvWelfare);
        } else {
            ImageLoadUtil.loadImage(this, getIntent().getStringExtra(EXTRA_BUNDLE_URL), 0, mIvWelfare);
        }
        mIvWelfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFade(toolbar);
            }
        });

        welfareDetailPresenter = new WelfareDetailPresenter(this, this);
    }

    private void toggleFade(Toolbar toolbar) {
        if (toolbar.getVisibility() == View.VISIBLE) {
            CommonUtil.animateOut(toolbar, R.anim.viewer_toolbar_fade_out);
            CommonUtil.animateOut(mViewStatusBar, R.anim.viewer_toolbar_fade_out);
            CommonUtil.setSystemUiVisible(this);
        } else {
            CommonUtil.animateIn(toolbar, R.anim.viewer_toolbar_fade_in);
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
                initPermission(0, getString(R.string.request_write_external_storage_permission), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.menu_save_img:
                initPermission(1, getString(R.string.request_write_external_storage_permission), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.menu_save_as_wallpaper:
                initPermission(2, getString(R.string.request_write_external_storage_permission), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPermission(final int type, String tip, String... permission) {
        performCodeWithPermission(tip, new PermissionCallback() {
            @Override
            public void hasPermission() {
                if (type == 0) {
                    welfareDetailPresenter.shareWelfare(getIntent().getStringExtra(EXTRA_BUNDLE_URL));
                } else if (type == 1) {
                    welfareDetailPresenter.saveWelfare(getIntent().getStringExtra(EXTRA_BUNDLE_URL));
                } else if (type == 2) {
                    welfareDetailPresenter.setWelfareToWallpaper(getIntent().getStringExtra(EXTRA_BUNDLE_URL));
                }
            }

            @Override
            public void noPermission() {
                Toast.makeText(WelfareDetailActivity.this, getString(R.string.you_have_refused_permission), Toast.LENGTH_SHORT).show();
            }
        }, permission);
    }

    @Override
    public void saveWelfareSuccess(String imgDir) {
        Toast.makeText(this, getString(R.string.welfare_had_save_to, imgDir), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWelfareToWallpaper(String imgPath) {
        /**
         * 对于面向 Android N 的应用，Android 框架执行的 StrictMode，API 禁止向您的应用外公开 file://URI。
         * 如果一项包含文件 URI 的 Intent 离开您的应用，应用失败，并出现 FileUriExposedException异常。
         * 若要在应用间共享文件，您应发送一项 content://URI，并授予 URI 临时访问权限。
         * 进行此授权的最简单方式是使用 FileProvider类。
         */
        WallpaperManager wm = WallpaperManager.getInstance(this);
        Uri uri = FileProvider.getUriForFile(this, "com.science.codegank.fileprovider", new File(imgPath));
        startActivityForResult(wm.getCropAndSetWallpaperIntent(uri), 1);
        //后台设置壁纸:
        //Bitmap bitmap = BitmapFactory.decodeFile(path);
        //wm.setBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        if (requestCode == 1) {
            Toast.makeText(this, getString(R.string.set_welfare_to_wallpaper_success), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
