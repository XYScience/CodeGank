package com.science.codegank.ui.fragment;

import android.view.View;

import com.science.codegank.R;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class HomeFragment extends BaseFragment {

    private View mRootView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void doCreateView(View view) {
        mRootView = view;
    }
}
