package com.science.codegank.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.science.codegank.R;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class SettingActivity extends BaseActivity {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(getString(R.string.setting));
    }
}
