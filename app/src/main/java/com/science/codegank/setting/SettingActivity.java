package com.science.codegank.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.MainActivity;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
