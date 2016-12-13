package com.science.codegank.module.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.science.codegank.MainActivity;
import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;

import butterknife.BindView;

/**
 * @author 幸运Science
 * @description 分类数据
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/10
 */

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_category;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(getString(R.string.category));

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(6);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
