package com.science.codegank.module.random;

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

public class RandomPresenter implements RandomContract.Presenter {

    private RandomContract.View mRandomView;

    public RandomPresenter(RandomContract.View randomView) {
        mRandomView = randomView;
        mRandomView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getRandomData(final String category, final int count) {
        Subscription subscription = HttpMethods.getInstance().getRandomData(category, count).
                subscribe(new MySubscriber<List<Gank>>() {
                    @Override
                    protected void onMyCompleted() {
                        mRandomView.refreshFinish();
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mRandomView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<Gank> ganks) {
                        if (ganks.isEmpty()) {
                            getRandomData(category, count);
                        } else {
                            mRandomView.getRandomData(ganks);
                            MyLogger.e(ganks.toString());
                        }
                    }
                });
        mRandomView.addSubscription(subscription);
    }
}
