package com.science.codegank.homeday;

import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.http.HttpMethods;
import com.science.codegank.http.MySubscriber;
import com.science.codegank.util.MyLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Func1;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/6
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mHomeView;
    private static final int DAY_OF_MILLISECOND = 24 * 60 * 60 * 1000;
    private Date mCurrentDate;
    private int mCountOfGetMoreDataEmpty = 0;
    private int gankPosition = 0;

    public HomePresenter(HomeContract.View homeView) {
        mHomeView = homeView;
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {
        getGankDayData(new Date(System.currentTimeMillis()));
    }

    @Override
    public void getGankDayData(final Date date) {
        mCurrentDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        gankPosition = 0;
        Subscription subscription = HttpMethods.getInstance().getGankDay(year, month, day - 1)
                .map(new Func1<GankDayResults, List<List<Gank>>>() {
                    @Override
                    public List<List<Gank>> call(GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new MySubscriber<List<List<Gank>>>() {
                    @Override
                    protected void onMyCompleted() {
                        mCurrentDate = new Date(date.getTime() - DAY_OF_MILLISECOND);
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mHomeView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<List<Gank>> ganks) {
                        if (ganks.isEmpty()) {
                            getGankDayData(new Date(date.getTime() - DAY_OF_MILLISECOND));
                        } else {
                            mHomeView.getGankDayData(ganks);
                            MyLogger.e(ganks.toString());
                        }
                    }
                });
        mHomeView.addSubscription(subscription);
    }

    @Override
    public void getGankDayDataMore() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCurrentDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Subscription subscription = HttpMethods.getInstance().getGankDay(year, month, day)
                .map(new Func1<GankDayResults, List>() {
                    @Override
                    public List call(GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new MySubscriber<List>() {
                    @Override
                    protected void onMyCompleted() {
                        mCurrentDate = new Date(mCurrentDate.getTime() - DAY_OF_MILLISECOND);
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mHomeView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List ganks) {
                        if (ganks.isEmpty()) {
                            mCountOfGetMoreDataEmpty += 1;
                            if (mCountOfGetMoreDataEmpty >= 8) {
                                mHomeView.hasNoMoreData();
                            } else {
                                getGankDayDataMore();
                            }
                        } else {
                            mCountOfGetMoreDataEmpty = 0;
                            mHomeView.getGankDayData(ganks);
                        }
                    }
                });
    }

    private List<List<Gank>> getAllResults(GankDayResults gankDayResults) {
        Map<Integer, List<Gank>> map = new HashMap<>();
        if (gankDayResults.福利 != null) {
            map.put(gankPosition, gankDayResults.福利);
            gankPosition++;
        }
        if (gankDayResults.Android != null) {
            map.put(gankPosition, gankDayResults.Android);
            gankPosition++;
        }
        if (gankDayResults.iOS != null) {
            map.put(gankPosition, gankDayResults.iOS);
            gankPosition++;
        }
        if (gankDayResults.前端 != null) {
            map.put(gankPosition, gankDayResults.前端);
            gankPosition++;
        }
        if (gankDayResults.拓展资源 != null) {
            map.put(gankPosition, gankDayResults.拓展资源);
            gankPosition++;
        }
        if (gankDayResults.瞎推荐 != null) {
            map.put(gankPosition, gankDayResults.瞎推荐);
            gankPosition++;
        }
        if (gankDayResults.App != null) {
            map.put(gankPosition, gankDayResults.App);
            gankPosition++;
        }
        if (gankDayResults.休息视频 != null) {
            map.put(gankPosition, gankDayResults.休息视频);
            gankPosition++;
        }
        List<List<Gank>> list = new ArrayList(map.values());
        return list;
    }
}
