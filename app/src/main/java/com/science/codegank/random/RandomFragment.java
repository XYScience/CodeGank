package com.science.codegank.random;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class RandomFragment extends BaseFragment implements RandomContract.View<Gank> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RandomAdapter mRandomAdapter;
    private RandomContract.Presenter mRandomPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_random;
    }

    @Override
    protected void doCreateView(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        mRandomAdapter = new RandomAdapter(getActivity());
        mRandomAdapter.setOnItemClickListener(new OnItemClickListener<Gank>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, Gank gank, int i) {
                Toast.makeText(getActivity(), gank.getDesc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemEmptyClick() {
                mRandomPresenter.getRandomData("all", 10);
            }
        });
        mRecyclerView.setAdapter(mRandomAdapter);
        mRandomPresenter.getRandomData("all", 10);

        initRefreshLayout(view);
    }

    @Override
    public void setPresenter(RandomContract.Presenter presenter) {
        if (presenter != null) {
            mRandomPresenter = presenter;
        }
    }

    @Override
    public void getRandomData(List<Gank> data) {
        mRandomAdapter.setData(false, data);
    }

    @Override
    public void getDataError(String msg) {
        setRefreshing(false);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        mRandomAdapter.showLoadFailed();
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    @Override
    public void onRefresh() {
        mRandomPresenter.getRandomData("all", 10);
    }

    @Override
    public void refreshFinish() {
        setRefreshing(false);
    }
}
