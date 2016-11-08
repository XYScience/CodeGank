package com.science.codegank.homeday;

import com.science.codegank.base.BasePresenter;
import com.science.codegank.base.BaseView;
import com.science.codegank.data.bean.BaseData;

import java.util.Date;
import java.util.List;

import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/6
 */

public interface HomeContract {

    interface View<T extends BaseData> extends BaseView<Presenter> {
        void getGankDayData(List<T> data);

        void addSubscription(Subscription subscription);

        void hasNoMoreData();
    }

    interface Presenter extends BasePresenter {
        void getGankDayData(Date date);

        void getGankDayDataMore();
    }
}
