package com.science.codegank.http;

import com.science.codegank.data.bean.GankDayEntity;
import com.science.codegank.data.bean.HttpResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/7
 */

public class HttpMethods {

    private static final String BASE_URL = "http://gank.io/api/";
    private CodeGank mCodeGank;

    public HttpMethods() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true) // 设置出现错误进行重新连接。
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mCodeGank = retrofit.create(CodeGank.class);
    }

    /**
     * 静态内部类获取单例
     *
     * @return
     */
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    /**
     * 获取每日干货数据
     *
     * @param year
     * @param month
     * @param day
     */
    public Observable<GankDayEntity> getGankDay(int year, int month, int day) {
        Observable<GankDayEntity> observable = mCodeGank.getGankDay(year, month, day)
                .compose(this.<GankDayEntity>applySchedulers());
        return observable;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.isError()) {
                // TODO: 2016/11/7
            }
            return httpResult.getResults();
        }
    }

    <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    final Observable.Transformer schedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
}









