package com.science.codegank.http;

import com.science.codegank.data.bean.GankDayEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/7
 */

public interface CodeGank {

    /**
     * 每日数据
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    @GET("/day/{year}/{month}/{day}")
    Observable<GankDayEntity> getGankDay(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}
