package com.science.codegank.data.bean;

import java.util.List;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/8
 */

public class GankDayResults extends BaseData{

    private String header;
    private List<Gank> gankList;
    public List<Gank> Android;
    public List<Gank> iOS;
    public List<Gank> 前端;
    public List<Gank> 拓展资源;
    public List<Gank> 瞎推荐;
    public List<Gank> App;
    public List<Gank> 福利;
    public List<Gank> 休息视频;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Gank> getGankList() {
        return gankList;
    }

    public void setGankList(List<Gank> gankList) {
        this.gankList = gankList;
    }

    public GankDayResults(String header, List<Gank> gankList) {
        this.header = header;
        this.gankList = gankList;
    }
}
