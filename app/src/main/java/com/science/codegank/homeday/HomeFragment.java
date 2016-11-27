package com.science.codegank.homeday;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.baserecyclerviewadapter.interfaces.OnLoadMoreListener;
import com.science.baserecyclerviewadapter.widget.StickyHeaderItemDecoration;
import com.science.codegank.MainActivity;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;
import com.science.codegank.util.MyLogger;
import com.science.codegank.util.customtabsutil.CustomTabActivityHelper;
import com.science.codegank.util.customtabsutil.WebViewFallback;
import com.science.codegank.view.OnDoubleClickListener;
import com.science.codegank.view.RatioImageView;
import com.science.codegank.welfaredetail.WelfareDetailActivity;

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

import static com.science.codegank.R.id.recyclerView;
import static com.science.codegank.R.id.toolbar;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class HomeFragment extends BaseFragment implements HomeContract.View<GankDayResults>,
        CustomTabActivityHelper.ConnectionCallback {

    public static final String EXTRA_BUNDLE_DESC = "bundle_desc";
    public static final String EXTRA_BUNDLE_URL = "bundle_url";
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private View mRootView;
    private HomeContract.Presenter mHomePresenter;
    private CustomTabActivityHelper customTabActivityHelper;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void doCreateView(final View view) {
        mRootView = view;
        customTabActivityHelper = new CustomTabActivityHelper();
        customTabActivityHelper.setConnectionCallback(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new StickyHeaderItemDecoration());
        mHomeAdapter = new HomeAdapter(getActivity());
        mHomeAdapter.setOnItemClickListener(new OnItemClickListener<GankDayResults>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, GankDayResults ganks, int i) {
                if (getResources().getString(R.string.welfare).equals(ganks.getHeader())) {
                    WelfareDetailActivity.intentWelfareDetail(getActivity(),
                            ganks.getGankList().get(i).getUrl(),
                            CommonUtil.toDate(ganks.getGankList().get(i).getPublishedAt()),
                            viewHolder.getView(R.id.iv_day_welfare));
                } else {
                    customTabActivityHelper.openCustomTab(getActivity(), ganks.getGankList().get(i),
                            customTabActivityHelper.getSession(), new WebViewFallback());
                }
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

        if (mHomePresenter != null) {
            mHomePresenter.start();
        }

        getActivity().findViewById(toolbar).setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onClicks(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        initRefreshLayout(view);
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
            final Gank gank = data.get(0).getGankList().get(0);
            customTabActivityHelper.mayLaunchUrl(Uri.parse(gank.getUrl()), null, null);
            String todayWelfareUrl = gank.getUrl();
            final RatioImageView ivWelfareToday = ((MainActivity) getActivity()).mIvWelfareToday;
            ImageLoadUtil.loadImage(getActivity(), todayWelfareUrl, R.drawable.welfare, ivWelfareToday);
            final TextView tvTimeToday = (TextView) getActivity().findViewById(R.id.tv_time_today);
            tvTimeToday.setText(CommonUtil.toDate(gank.getPublishedAt()));
            CommonUtil.animateIn(tvTimeToday, R.anim.viewer_toolbar_fade_in);
            data.remove(0);
            mHomeAdapter.setData(false, data);
            ivWelfareToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WelfareDetailActivity.intentWelfareDetail(getActivity(),
                            gank.getUrl(),
                            CommonUtil.toDate(gank.getPublishedAt()),
                            ivWelfareToday);
                }
            });
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
        setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mHomePresenter.start();
    }

    @Override
    public void refreshFinish() {
        setRefreshing(false);
    }

    @Override
    public void onCustomTabsConnected() {
        MyLogger.e("onCustomTabsConnected()");
    }

    @Override
    public void onCustomTabsDisconnected() {
        MyLogger.e("onCustomTabsDisconnected()");
    }

    @Override
    public void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }
}
