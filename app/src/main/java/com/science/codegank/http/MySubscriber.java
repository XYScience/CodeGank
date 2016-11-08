package com.science.codegank.http;

import com.science.codegank.util.MyLogger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/8
 */

public abstract class MySubscriber<T> extends Subscriber<T> {

    protected abstract void onMyCompleted();

    protected abstract void onMyError(String msg);

    protected abstract void onMyNext(T t);

    @Override
    public void onCompleted() {
        onMyCompleted();
    }

    @Override
    public void onError(Throwable e) {
        String msg = "未知错误/(ㄒoㄒ)/~~";
        if (e instanceof HttpException) {
            msg = "网络请求超时/(ㄒoㄒ)/~~";
        } else if (e instanceof UnknownHostException) {
            msg = "无网络连接/(ㄒoㄒ)/~~";
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时/(ㄒoㄒ)/~~";
        } else if (e instanceof ConnectException) {
            msg = "网络连接不上/(ㄒoㄒ)/~~";
        } else {
            MyLogger.e("onError:" + e.toString());
        }
        onMyError(msg);
    }

    @Override
    public void onNext(T t) {
        onMyNext(t);
    }
}
