package com.science.codegank.searchresult;

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

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class SearchResultFragment extends BaseFragment implements SearchResultContract.View<SearchResult> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private SearchResultAdapter mSearchResultAdapter;
    private SearchResultContract.Presenter mSearchResultPresenter;
    private String query;

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
        mSearchResultAdapter = new SearchResultAdapter(getActivity());
        mSearchResultAdapter.setOnItemClickListener(new OnItemClickListener<SearchResult>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, SearchResult searchResult, int i) {
                Toast.makeText(getActivity(), searchResult.getDesc(), Toast.LENGTH_SHORT).show();
            }
        });
        mSearchResultAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i) {
                searchGank(query, i);
            }
        });
        mRecyclerView.setAdapter(mSearchResultAdapter);

        initRefreshLayout(view);
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
}