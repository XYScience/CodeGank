package com.science.codegank.category.restvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.category.CategoryFragment;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class RestVideoActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_random;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(getString(R.string.rest_video));
        CategoryFragment restVideoFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(CategoryFragment.TAB_CATEGORY, getString(R.string.rest_video));
        restVideoFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.content_random, restVideoFragment).commit();
    }
}
