package com.science.codegank.random;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/11
 */

public class RandomActivity extends BaseActivity {

    private RandomFragment mRandomFragment;
    private Toolbar mToolbar;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_random;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = setToolbar(getString(R.string.random_, getString(R.string.all)));
        mRandomFragment = new RandomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_random, mRandomFragment).commit();
        new RandomPresenter(mRandomFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_all:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.all), 10);
                break;
            case R.id.menu_android:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.android), 10);
                break;
            case R.id.menu_ios:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.ios), 10);
                break;
            case R.id.menu_web:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.web), 10);
                break;
            case R.id.menu_other_resource:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.other_resource), 10);
                break;
            case R.id.menu_more:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.more), 10);
                break;
            case R.id.menu_rest_video:
                mRandomFragment.getRandomDataFromMenu(mToolbar, getResources().getString(R.string.rest_video), 10);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
