package com.science.codegank.homeday;

import com.science.codegank.R;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.http.HttpMethods;
import com.science.codegank.http.MySubscriber;
import com.science.codegank.util.MyLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        MyLogger.e("今天：" + year + "-" + month + "-" + day);
        Subscription subscription = HttpMethods.getInstance().getGankDay(year, month, day)
                .map(new Func1<GankDayResults, List<GankDayResults>>() {
                    @Override
                    public List<GankDayResults> call(GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new MySubscriber<List<GankDayResults>>() {
                    @Override
                    protected void onMyCompleted() {
                        mCurrentDate = new Date(date.getTime() - DAY_OF_MILLISECOND);
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mHomeView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<GankDayResults> ganks) {
                        if (ganks.isEmpty()) {
                            getGankDayData(new Date(date.getTime() - DAY_OF_MILLISECOND));
                        } else {
                            mHomeView.getGankDayData(true, ganks);
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
                .map(new Func1<GankDayResults, List<GankDayResults>>() {
                    @Override
                    public List<GankDayResults> call(GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new MySubscriber<List<GankDayResults>>() {
                    @Override
                    protected void onMyCompleted() {
                        mCurrentDate = new Date(mCurrentDate.getTime() - DAY_OF_MILLISECOND);
                    }

                    @Override
                    protected void onMyError(String msg) {
                        mHomeView.getDataError(msg);
                    }

                    @Override
                    protected void onMyNext(List<GankDayResults> ganks) {
                        if (ganks.isEmpty()) {
                            mCountOfGetMoreDataEmpty += 1;
                            if (mCountOfGetMoreDataEmpty >= 8) {
                                mHomeView.hasNoMoreData();
                            } else {
                                getGankDayDataMore();
                            }
                        } else {
                            mCountOfGetMoreDataEmpty = 0;
                            mHomeView.getGankDayData(false, ganks);
                        }
                    }
                });
    }

    private List<GankDayResults> getAllResults( GankDayResults gankDayResults) {
        List<GankDayResults> gankDayResultses = new ArrayList<>();
        if (gankDayResults.福利 != null) {
            gankDayResultses.add(new GankDayResults("福利", R.drawable.ic_welfare_white, gankDayResults.福利));
        }
        if (gankDayResults.Android != null) {
            gankDayResultses.add(new GankDayResults("Android", R.drawable.ic_android_white, gankDayResults.Android));
        }
        if (gankDayResults.iOS != null) {
            gankDayResultses.add(new GankDayResults("iOS", R.drawable.ic_ios_white, gankDayResults.iOS));
        }
        if (gankDayResults.前端 != null) {
            gankDayResultses.add(new GankDayResults("前端", R.drawable.ic_web_white, gankDayResults.前端));
        }
        if (gankDayResults.拓展资源 != null) {
            gankDayResultses.add(new GankDayResults("拓展资源", R.drawable.ic_resource_white, gankDayResults.拓展资源));
        }
        if (gankDayResults.瞎推荐 != null) {
            gankDayResultses.add(new GankDayResults("瞎推荐", R.drawable.ic_more_white, gankDayResults.瞎推荐));
        }
        if (gankDayResults.App != null) {
            gankDayResultses.add(new GankDayResults("App", R.drawable.ic_apps_white, gankDayResults.App));
        }
        if (gankDayResults.休息视频 != null) {
            gankDayResultses.add(new GankDayResults("休息视频", R.drawable.ic_video_library_white, gankDayResults.休息视频));
        }
        return gankDayResultses;
    }
}
