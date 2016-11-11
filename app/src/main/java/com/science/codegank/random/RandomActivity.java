package com.science.codegank.random;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/11
 */

public class RandomActivity extends BaseActivity {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_random;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(getString(R.string.random));
        RandomFragment randomFragment = new RandomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_random, randomFragment).commit();
        new RandomPresenter(randomFragment);
    }
}
