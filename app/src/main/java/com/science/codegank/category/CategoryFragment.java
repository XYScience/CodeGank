package com.science.codegank.category;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.baserecyclerviewadapter.interfaces.OnLoadMoreListener;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.util.MyLogger;
import com.science.codegank.util.customtabsutil.CustomTabActivityHelper;
import com.science.codegank.util.customtabsutil.WebViewFallback;

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/10
 */

public class CategoryFragment extends BaseFragment implements CategoryContract.View<Gank>, CustomTabActivityHelper.ConnectionCallback {

    public static final String TAB_CATEGORY = "tab_category";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private CategoryContract.Presenter mCategoryPresenter;
    private CustomTabActivityHelper customTabActivityHelper;

    public static CategoryFragment newInstance(String tabCategory) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(TAB_CATEGORY, tabCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void doCreateView(View view) {
        customTabActivityHelper = new CustomTabActivityHelper();
        customTabActivityHelper.setConnectionCallback(this);
        final Bundle args = getArguments();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        mCategoryAdapter = new CategoryAdapter(getActivity());
        mCategoryAdapter.setOnItemClickListener(new OnItemClickListener<Gank>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, Gank gank, int i) {
                customTabActivityHelper.openCustomTab(getActivity(), gank,
                        customTabActivityHelper.getSession(), new WebViewFallback());
            }

            @Override
            public void onItemEmptyClick() {
                mCategoryPresenter.getCategoryData(args.getString(TAB_CATEGORY), 1);
            }
        });
        mCategoryAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i) {
                mCategoryPresenter.getCategoryData(args.getString(TAB_CATEGORY), i);
            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);

        new CategoryPresenter(this);
        mCategoryPresenter.getCategoryData(args.getString(TAB_CATEGORY), 1);

        initRefreshLayout(view);
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        if (presenter != null) {
            mCategoryPresenter = presenter;
        }
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    @Override
    public void getCategoryData(boolean isFirst, List<Gank> data) {
        if (isFirst) {
            mCategoryAdapter.setData(false, data);
        } else {
            mCategoryAdapter.setData(true, data);
        }
    }

    @Override
    public void getDataError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        setRefreshing(false);
        mCategoryAdapter.showLoadFailed();
    }

    @Override
    public void onRefresh() {
        mCategoryPresenter.getCategoryData(getArguments().getString(TAB_CATEGORY), 1);
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
