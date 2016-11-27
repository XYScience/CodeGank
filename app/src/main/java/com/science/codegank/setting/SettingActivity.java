package com.science.codegank.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.science.codegank.MainActivity;
import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.util.CommonDefine;
import com.science.codegank.util.SharedPreferenceUtil;
import com.science.codegank.util.customtabsutil.CustomTabsHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.cb_smart_no_pic_model)
    AppCompatCheckBox mCbSmartNoPicModel;
    @BindView(R.id.cb_chrome_custom_tab)
    AppCompatCheckBox mCbChromeCustomTab;
    @BindView(R.id.cb_quit_clear_cache)
    AppCompatCheckBox mCbQuitClearCache;
    @BindView(R.id.ll_clear_cache)
    LinearLayout mLlClearCache;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(getString(R.string.setting));
        initView();
        initListener();
    }

    private void initView() {
        mCbSmartNoPicModel.setChecked((Boolean) SharedPreferenceUtil.get(this, CommonDefine.SP_KEY_SMART_NO_PIC, false));
        mCbChromeCustomTab.setChecked((Boolean) SharedPreferenceUtil.get(this, CommonDefine.SP_KEY_CHROME_CUSTOM_TAB, false));
        mCbQuitClearCache.setChecked((Boolean) SharedPreferenceUtil.get(this, CommonDefine.SP_KEY_QUIT_CLEAR_CACHE, false));
    }

    private void initListener() {
        mCbSmartNoPicModel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!mCbChromeCustomTab.isChecked()) {
                    SharedPreferenceUtil.put(SettingActivity.this, CommonDefine.SP_KEY_SMART_NO_PIC, b);
                } else {
                    compoundButton.setChecked(false);
                    Snackbar.make(mCoordinatorLayout, R.string.smart_no_pic_support_webview,
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mCbChromeCustomTab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String packageName = CustomTabsHelper.getPackageNameToUse(SettingActivity.this);
                if (packageName == null) {
                    compoundButton.setChecked(false);
                    Snackbar.make(mCoordinatorLayout, R.string.custom_tabs_need_chrome, Snackbar.LENGTH_LONG).
                            setAction(R.string.go_to_download_chrome, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Uri uri = Uri.parse("market://details?id=" + CustomTabsHelper.STABLE_PACKAGE);
                                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(goToMarket);
                                }
                            }).show();
                } else {
                    mCbSmartNoPicModel.setChecked(false);
                    SharedPreferenceUtil.put(SettingActivity.this, CommonDefine.SP_KEY_CHROME_CUSTOM_TAB, b);
                }
            }
        });

        mCbQuitClearCache.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferenceUtil.put(SettingActivity.this, CommonDefine.SP_KEY_QUIT_CLEAR_CACHE, b);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.ll_clear_cache)
    public void onClick() {

    }
}
