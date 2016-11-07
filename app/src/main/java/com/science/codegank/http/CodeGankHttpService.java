package com.science.codegank.http;

import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayEntity;
import com.science.codegank.data.bean.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/7
 */

public interface CodeGankHttpService {

    /**
     * 每日数据
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    @GET("day/{year}/{month}/{day}")
    Observable<GankDayEntity> getGankDay(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    /**
     * 分类数据
     * @param category
     * @param page
     * @return
     */
    @GET("data/{category}/10/{page}")
    Observable<HttpResult<List<Gank>>> getCategory(@Path("category") int category, @Path("page") int page);
}
