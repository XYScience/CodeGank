package com.science.codegank.data.bean;

import java.util.List;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/7
 */

public class Gank extends BaseData{
    /**
     * _id : "57dfafae421aa95bc7f06a60"
     * createdAt : "2016-09-19T17:28:14.261Z"
     * desc : "任阅小说阅读器，高仿追书神器，实现追书推荐、标签检索、3D仿真翻页效果、文章阅读、缓存章节、日夜间模式、文本朗读等功能。"
     * images : ["http://img.gank.io/ecab85fd-68b1-4035-a146-53a8d51e94af","http://img.gank.io/7ee327fc-c6e6-46c0-9689-f713adc51cb4"]
     * publishedAt : "2016-09-23T11:38:57.170Z"
     * source : "web"
     * type : "Android"
     * url : https://github.com/JustWayward/BookReader
     * used : true
     * who : "LeBron_Six"
     */

    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
    private List<String> images;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
