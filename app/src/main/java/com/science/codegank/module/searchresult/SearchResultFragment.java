package com.science.codegank.module.searchresult;

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
import com.science.codegank.data.bean.SearchResult;
import com.science.codegank.util.MyLogger;
import com.science.codegank.util.customtabsutil.CustomTabActivityHelper;
import com.science.codegank.util.customtabsutil.WebViewFallback;

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class SearchResultFragment extends BaseFragment implements SearchResultContract.View<SearchResult>,
        CustomTabActivityHelper.ConnectionCallback {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private SearchResultAdapter mSearchResultAdapter;
    private SearchResultContract.Presenter mSearchResultPresenter;
    private String query;
    private CustomTabActivityHelper customTabActivityHelper;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_random;
    }

    @Override
    protected void doCreateView(View view) {
        customTabActivityHelper = new CustomTabActivityHelper();
        customTabActivityHelper.setConnectionCallback(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        mSearchResultAdapter = new SearchResultAdapter(getActivity(), mRecyclerView);
        mSearchResultAdapter.setOnItemClickListener(new OnItemClickListener<SearchResult>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, SearchResult searchResult, int i) {
                customTabActivityHelper.openCustomTab(getActivity(), searchResult,
                        customTabActivityHelper.getSession(), new WebViewFallback());
            }
        });
        mSearchResultAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i) {
                setSwipeRefreshEnable(false);
                searchGank(query, i);
            }
        });
        mRecyclerView.setAdapter(mSearchResultAdapter);

        initRefreshLayout(view);
        setSwipeRefreshEnable(false);
    }

    public void searchGank(String query, int page) {
        this.query = query;
        mSearchResultPresenter.getSearchResultData(query, "Android", page);
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        if (presenter != null) {
            mSearchResultPresenter = presenter;
        }
    }

    @Override
    public void getSearchResultData(boolean isFirst, List<SearchResult> data) {
        setSwipeRefreshEnable(true);
        if (isFirst) {
            mSearchResultAdapter.setData(false, data);
        } else {
            mSearchResultAdapter.setData(true, data);
        }
    }

    @Override
    public void onRefresh() {
        searchGank(query, 1);
    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void refreshFinish() {
        setRefreshing(false);
    }

    @Override
    public void getDataError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        setRefreshing(false);
        mSearchResultAdapter.showLoadFailed();
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
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
