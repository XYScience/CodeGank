package com.science.codegank.module.category.welfare;

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
 * @data 2016/11/12
 */

public class WelfareListPresenter implements WelfareListContract.Presenter {

    private int mCountOfGetMoreDataEmpty = 0;
    private WelfareListContract.View mWelfareView;

    public WelfareListPresenter(WelfareListContract.View welfareView) {
        mWelfareView = welfareView;
        mWelfareView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void getWelfareData(final String category, final int page) {
        Subscription subscription = HttpMethods.getInstance().getCategory(category, page)
                .subscribe(new MySubscriber<List<Gank>>() {
                    @Override
                    protected void onMyCompleted() {
                        mWelfareView.refreshFinish();
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mWelfareView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<Gank> ganks) {
                        if (ganks.isEmpty()) {
                            mCountOfGetMoreDataEmpty += 1;
                            if (mCountOfGetMoreDataEmpty >= 8) {
                                mWelfareView.hasNoMoreData();
                            } else {
                                getWelfareData(category, page);
                            }
                        } else {
                            mCountOfGetMoreDataEmpty = 0;
                            if (page == 1) {
                                mWelfareView.getWelfareData(true, ganks);
                            } else {
                                mWelfareView.getWelfareData(false, ganks);
                            }
                            MyLogger.e(ganks.toString());
                        }
                    }
                });
        mWelfareView.addSubscription(subscription);
    }
}
