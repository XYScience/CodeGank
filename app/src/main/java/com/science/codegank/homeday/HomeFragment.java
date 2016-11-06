package com.science.codegank.homeday;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;
    private HomeContract.Presenter mHomePresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void doCreateView(View view) {
        mRootView = view;
        ButterKnife.bind(this, mRootView);


    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        if (presenter != null) {
            mHomePresenter = presenter;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomePresenter.start();
    }
}
