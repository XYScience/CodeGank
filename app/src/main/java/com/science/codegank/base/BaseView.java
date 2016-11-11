package com.science.codegank.base;

import rx.Subscription;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/9/26
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    void getDataError(String msg);
    void addSubscription(Subscription subscription);
}
