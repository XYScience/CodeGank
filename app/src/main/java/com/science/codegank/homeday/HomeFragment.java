package com.science.codegank.homeday;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.baserecyclerviewadapter.interfaces.OnLoadMoreListener;
import com.science.baserecyclerviewadapter.widget.StickyHeaderItemDecoration;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.util.ImageLoadUtil;

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

public class HomeFragment extends BaseFragment implements HomeContract.View<GankDayResults> {

    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
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
        mRecyclerView.addItemDecoration(new StickyHeaderItemDecoration());
        mHomeAdapter = new HomeAdapter(getActivity());
        mHomeAdapter.setOnItemClickListener(new OnItemClickListener<GankDayResults>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, GankDayResults ganks, int i) {
                Toast.makeText(getActivity(), ganks.getGankList().get(i).getDesc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemEmptyClick() {
                mHomePresenter.start();
            }
        });
        mHomeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i) {
                mHomePresenter.getGankDayDataMore();
            }
        });
        mRecyclerView.setAdapter(mHomeAdapter);

        mHomePresenter.start();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        if (presenter != null) {
            mHomePresenter = presenter;
        }
    }

    @Override
    public void getGankDayData(boolean isFirst, List<GankDayResults> data) {
        if (isFirst) {
            String todayWelfareUrl = data.get(0).getGankList().get(0).getUrl();
            ImageView ivWelfareToday = (ImageView) getActivity().findViewById(R.id.iv_welfare_today);
            ImageLoadUtil.loadImage(getActivity(), todayWelfareUrl, R.drawable.welfare, ivWelfareToday);
            TextView tvTimeToday = (TextView) getActivity().findViewById(R.id.tv_time_today);
            String timeToday = data.get(0).getGankList().get(0).getPublishedAt();
            String[] s = timeToday.split("T");
            tvTimeToday.setText(s[0]);
            data.remove(0);
            mHomeAdapter.setData(false, data);
        } else {
            mHomeAdapter.setData(true, data);
        }
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    @Override
    public void hasNoMoreData() {
        mHomeAdapter.showFooterNoMoreData();
    }

    @Override
    public void getDataError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        mHomeAdapter.showLoadFailed();
    }
}
