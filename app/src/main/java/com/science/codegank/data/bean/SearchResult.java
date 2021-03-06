package com.science.codegank.data.bean;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class SearchResult extends BaseData {

    /**
     * desc : listview的折叠效果
     * ganhuo_id : 56cc6d1d421aa95caa7076fa
     * publishedAt : 2015-07-17T03:43:22.395000
     * readability : ListView
     * type : Android
     * url : https://github.com/dodola/ListItemFold
     * who : Jason
     */

    private String desc;
    private String ganhuo_id;
    private String publishedAt;
    private String readability;
    private String type;
    private String url;
    private String who;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGanhuo_id() {
        return ganhuo_id;
    }

    public void setGanhuo_id(String ganhuo_id) {
        this.ganhuo_id = ganhuo_id;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getReadability() {
        return readability;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
