package com.science.codegank.category.welfare;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class WelfareListActivity extends BaseActivity{
    @Override
    protected int getContentLayout() {
        return R.layout.activity_random;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(getString(R.string.welfare));
        WelfareListFragment welfareFragment = new WelfareListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_random, welfareFragment).commit();
        new WelfareListPresenter(welfareFragment);
    }
}
