package com.science.codegank.homeday;

import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.http.HttpMethods;
import com.science.codegank.util.MyLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
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
        MyLogger.e(year + "-" + month + "-" + day);
        Subscription subscription = HttpMethods.getInstance().getGankDay(year, month, day)
                .map(new Func1<GankDayResults, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new Subscriber<List<Gank>>() {
                    @Override
                    public void onCompleted() {
                        mCurrentDate = new Date(date.getTime() - DAY_OF_MILLISECOND);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLogger.e("onError:" + e.toString());
                    }

                    @Override
                    public void onNext(List<Gank> ganks) {
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
                .map(new Func1<GankDayResults, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new Subscriber<List<Gank>>() {
                    @Override
                    public void onCompleted() {
                        mCurrentDate = new Date(mCurrentDate.getTime() - DAY_OF_MILLISECOND);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLogger.e("onError:" + e.toString());
                    }

                    @Override
                    public void onNext(List<Gank> ganks) {
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

    private List<Gank> getAllResults(GankDayResults gankDayResults) {
        Map<Integer, List<Gank>> map = new HashMap<>();
        int i = 0;
        List<Gank> gankList = new ArrayList<>();
        if (gankDayResults.Android != null) {
            gankList.addAll(gankDayResults.Android);
            map.put(i, gankDayResults.Android);
            MyLogger.e(map.get(i).size());
            i++;
        }
        if (gankDayResults.iOS != null) {
            gankList.addAll(gankDayResults.iOS);
            map.put(i, gankDayResults.iOS);
            MyLogger.e(map.get(i).size());
            i++;
        }
        if (gankDayResults.前端 != null) {
            gankList.addAll(gankDayResults.前端);
            map.put(i, gankDayResults.前端);
            MyLogger.e(map.get(i).size());
            i++;
        }
        if (gankDayResults.拓展资源 != null) {
            gankList.addAll(gankDayResults.拓展资源);
            map.put(i, gankDayResults.拓展资源);
            MyLogger.e(map.get(i).size());
            MyLogger.e(map.size());
            i++;
        }
        if (gankDayResults.瞎推荐 != null) {
            gankList.addAll(gankDayResults.瞎推荐);
        }
        if (gankDayResults.App != null) {
            gankList.addAll(gankDayResults.App);
        }
        if (gankDayResults.休息视频 != null) {
            gankList.addAll(gankDayResults.休息视频);
        }
        if (gankDayResults.福利 != null) {
            gankList.addAll(0, gankDayResults.福利);
        }
        return gankList;
    }
}
