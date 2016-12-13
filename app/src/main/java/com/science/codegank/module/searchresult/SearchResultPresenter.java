package com.science.codegank.module.searchresult;

import com.science.codegank.data.bean.SearchResult;
import com.science.codegank.http.HttpMethods;
import com.science.codegank.http.MySubscriber;
import com.science.codegank.util.MyLogger;

import java.util.List;

import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class SearchResultPresenter implements SearchResultContract.Presenter {

    private SearchResultContract.View mSearchResultView;

    public SearchResultPresenter(SearchResultContract.View searchResultView) {
        mSearchResultView = searchResultView;
        mSearchResultView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getSearchResultData(String query, final String category, final int page) {
        Subscription subscription = HttpMethods.getInstance().getSearchResultData(query, category, page)
                .subscribe(new MySubscriber<List<SearchResult>>() {
                    @Override
                    protected void onMyCompleted() {
                        mSearchResultView.refreshFinish();
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mSearchResultView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<SearchResult> searchResults) {
                        if (searchResults.isEmpty()) {
                            getSearchResultData(category, category, page);
                        } else {
                            if (page == 1) {
                                mSearchResultView.getSearchResultData(true, searchResults);
                            } else {
                                mSearchResultView.getSearchResultData(false, searchResults);
                            }
                            MyLogger.e(searchResults.toString());
                        }
                    }
                });
        mSearchResultView.addSubscription(subscription);
    }
}
