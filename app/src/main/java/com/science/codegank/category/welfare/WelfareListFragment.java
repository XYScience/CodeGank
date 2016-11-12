package com.science.codegank.category.welfare;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.baserecyclerviewadapter.interfaces.OnLoadMoreListener;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/3
 */

public class WelfareListFragment extends BaseFragment implements WelfareListContract.View<Gank> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private WelfareListAdapter mWelfareAdapter;
    private WelfareListContract.Presenter mWelfarePresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_random;
    }

    @Override
    protected void doCreateView(View view) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setPadding(CommonUtil.dipToPx(getActivity(), 4), 0, CommonUtil.dipToPx(getActivity(), 4), CommonUtil.dipToPx(getActivity(), 16));
        mWelfareAdapter = new WelfareListAdapter(getActivity());
        mWelfareAdapter.setOnItemClickListener(new OnItemClickListener<Gank>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, Gank gank, int i) {
                Toast.makeText(getActivity(), gank.getDesc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemEmptyClick() {
                mWelfarePresenter.getWelfareData(getString(R.string.welfare), 1);
            }
        });
        mWelfareAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i) {
                mWelfarePresenter.getWelfareData(getString(R.string.welfare), i);
            }
        });
        mRecyclerView.setAdapter(mWelfareAdapter);
        mWelfarePresenter.getWelfareData(getString(R.string.welfare), 1);

        initRefreshLayout(view);
    }

    @Override
    public void setPresenter(WelfareListContract.Presenter presenter) {
        if (presenter != null) {
            mWelfarePresenter = presenter;
        }
    }

    @Override
    public void onRefresh() {
        mWelfarePresenter.getWelfareData(getString(R.string.welfare), 1);
    }

    @Override
    public void getWelfareData(boolean isFirst, List<Gank> data) {
        if (isFirst) {
            mWelfareAdapter.setData(false, data);
        } else {
            mWelfareAdapter.setData(true, data);
        }
    }

    @Override
    public void getDataError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        setRefreshing(false);
        mWelfareAdapter.showLoadFailed();
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    @Override
    public void refreshFinish() {
        setRefreshing(false);
    }
}
