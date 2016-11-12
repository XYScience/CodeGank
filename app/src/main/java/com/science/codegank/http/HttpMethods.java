package com.science.codegank.http;

import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.data.bean.HttpResult;
import com.science.codegank.data.bean.SearchResult;

import java.util.List;
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
    private CodeGankHttpService mCodeGankHttpService;

    public HttpMethods() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true) // 设置出现错误进行重新连接。
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mCodeGankHttpService = retrofit.create(CodeGankHttpService.class);
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    /**
     * 静态内部类获取单例
     *
     * @return
     */
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取每日干货数据
     *
     * @param year
     * @param month
     * @param day
     */
    public Observable<GankDayResults> getGankDay(int year, int month, int day) {
        Observable<GankDayResults> observable = mCodeGankHttpService.getGankDay(year, month, day)
                .compose(this.<HttpResult<GankDayResults>>applySchedulers())
                .map(new HttpResultFunc<GankDayResults>());
        return observable;
    }

    /**
     * 分类数据
     *
     * @param category 数据类型： ["Android","瞎推荐","前端","拓展资源","iOS","福利","休息视频", "App", "all"]
     * @param page     第几页：数字，大于0
     * @return
     */
    public Observable<List<Gank>> getCategory(String category, int page) {
        Observable<List<Gank>> observable = mCodeGankHttpService.getCategory(category, page)
                .compose(this.<HttpResult<List<Gank>>>applySchedulers())
                .map(new HttpResultFunc<List<Gank>>());
        return observable;
    }

    /**
     * 随机数据
     *
     * @param category 数据类型： ["Android","瞎推荐","前端","拓展资源","iOS","福利","休息视频", "App", "all]
     * @param count    个数： 数字，大于0
     * @return
     */
    public Observable<List<Gank>> getRandomData(String category, int count) {
        Observable<List<Gank>> observable = mCodeGankHttpService.getRandomData(category, count)
                .compose(this.<HttpResult<List<Gank>>>applySchedulers())
                .map(new HttpResultFunc<List<Gank>>());
        return observable;
    }

    /**
     * 搜索数据
     * @param query 搜索关键字
     * @param category 搜索分类
     * @param page 搜索页数
     * @return
     */
    public Observable<List<SearchResult>> getSearchResultData(String query, String category, int page) {
        Observable<List<SearchResult>> observable = mCodeGankHttpService.getSearchResult(query, category, page)
                .compose(this.<HttpResult<List<SearchResult>>>applySchedulers())
                .map(new HttpResultFunc<List<SearchResult>>());
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









