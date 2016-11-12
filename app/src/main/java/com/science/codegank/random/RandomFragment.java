package com.science.codegank.random;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    private Toolbar mToolbar;
    private String mCategory;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_random;
    }

    @Override
    protected void doCreateView(View view) {
        mCategory = getString(R.string.all);
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
                mRandomPresenter.getRandomData(mCategory, 10);
            }
        });
        mRecyclerView.setAdapter(mRandomAdapter);
        mRandomPresenter.getRandomData(mCategory, 10);

        initRefreshLayout(view);
    }

    /**
     * 在RandomActivity调用
     *
     * @param toolbar
     * @param category
     * @param count
     */
    public void getRandomDataFromMenu(Toolbar toolbar, String category, int count) {
        mToolbar = toolbar;
        mCategory = category;
        setRefreshing(true);
        mRandomAdapter.setCategory(mCategory);
        mRandomPresenter.getRandomData(category, count);
    }

    @Override
    public void setPresenter(RandomContract.Presenter presenter) {
        if (presenter != null) {
            mRandomPresenter = presenter;
        }
    }

    @Override
    public void getRandomData(List<Gank> data) {
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.random_, mCategory));
        }
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
        mRandomPresenter.getRandomData(mCategory, 10);
    }

    @Override
    public void refreshFinish() {
        setRefreshing(false);
    }
}
