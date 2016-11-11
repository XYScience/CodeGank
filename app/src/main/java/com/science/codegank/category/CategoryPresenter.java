package com.science.codegank.category;

import com.science.codegank.data.bean.Gank;
import com.science.codegank.http.HttpMethods;
import com.science.codegank.http.MySubscriber;
import com.science.codegank.util.MyLogger;

import java.util.List;

import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/10
 */

public class CategoryPresenter implements CategoryContract.Presenter {

    private CategoryContract.View mCategoryView;

    public CategoryPresenter(CategoryContract.View categoryView) {
        mCategoryView = categoryView;
        mCategoryView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void getCategoryData(final String category, final int page) {
        Subscription subscription = HttpMethods.getInstance().getCategory(category, page)
                .subscribe(new MySubscriber<List<Gank>>() {
                    @Override
                    protected void onMyCompleted() {
                        mCategoryView.refreshFinish();
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mCategoryView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<Gank> ganks) {
                        if (ganks.isEmpty()) {
                            getCategoryData(category, page);
                        } else {
                            if (page == 1) {
                                mCategoryView.getCategoryData(true, ganks);
                            } else {
                                mCategoryView.getCategoryData(false, ganks);
                            }
                            MyLogger.e(ganks.toString());
                        }
                    }
                });
        mCategoryView.addSubscription(subscription);
    }
}
