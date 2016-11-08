package com.science.codegank.homeday;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

import static com.science.codegank.R.id.recyclerView;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class HomeFragment extends BaseFragment implements HomeContract.View<Gank> {

    @BindView(recyclerView)
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

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.addItemDecoration(new StickyHeaderItemDecoration());
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


    @Override
    public void getGankDayData(List<Gank> data) {

    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    @Override
    public void hasNoMoreData() {

    }
}
