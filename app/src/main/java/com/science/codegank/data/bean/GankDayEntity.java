package com.science.codegank.data.bean;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/9/26
 */

public class GankDayEntity {

    /**
     * category : ["Android","瞎推荐","前端","拓展资源","iOS","福利","休息视频", "App"]
     * error : false
     * results : {
     * "Android":[
     * {"_id":"57dfafae421aa95bc7f06a60",
     * "createdAt":"2016-09-19T17:28:14.261Z",
     * "desc":"任阅小说阅读器，高仿追书神器，实现追书推荐、标签检索、3D仿真翻页效果、文章阅读、缓存章节、日夜间模式、文本朗读等功能。",
     * "images":["http://img.gank.io/ecab85fd-68b1-4035-a146-53a8d51e94af","http://img.gank.io/7ee327fc-c6e6-46c0-9689-f713adc51cb4"],
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"web",
     * "type":"Android",
     * "url":"https://github.com/JustWayward/BookReader",
     * "used":true,"who":"LeBron_Six"}
     * ],
     * "iOS":[
     * {"_id":"57e4735c421aa95bc3389878",
     * "createdAt":"2016-09-23T08:12:12.310Z",
     * "desc":"让所有 UIView 都支持进度条展示",
     * "images":["http://img.gank.io/509bc330-fa97-4e54-b460-8ac7bf685cc7"],
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"chrome",
     * "type":"iOS",
     * "url":"https://github.com/MartinMoizard/Progressable",
     * "used":true,"who":"代码家"}
     * ],
     * "休息视频":[
     * {"_id":"57e47890421aa95bc7f06a96",
     * "createdAt":"2016-09-23T08:34:24.453Z",
     * "desc":"以一个女儿的口吻向父亲讲诉成长的每个阶段遇到的种种欺凌，但其实一切都能从源头改变。",
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"chrome",
     * "type":"休息视频",
     * "url":"http://v.youku.com/v_show/id_XMTczMjE2MTMzMg==.html","used":true,"who":"Robot"}
     * ],
     * "前端":[
     * {"_id":"57e365ce421aa95bc3389870",
     * "createdAt":"2016-09-22T13:02:06.936Z",
     * "desc":"可以将时间戳格式化为  *** 秒前，*** 分钟前, *** 小时前",
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"chrome",
     * "type":"前端",
     * "url":"https://github.com/hustcc/timeago.js",
     * "used":true,
     * "who":"jk2K"}
     * ],
     * "拓展资源":[
     * {"_id":"57e373bd421aa95bcb130172",
     * "createdAt":"2016-09-22T14:01:33.693Z",
     * "desc":"Primitive，用基本几何图形重绘图像",
     * "images":["http://img.gank.io/fad2d087-272a-4ea4-aae9-fbebfb746795","http://img.gank.io/576e9b08-c671-44ec-b6ab-88e4bdceaefb"],
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"web",
     * "type":"拓展资源",
     * "url":"https://github.com/fogleman/primitive",
     * "used":true,
     * "who":"drakeet"}
     * ],
     * "瞎推荐":[
     * {"_id":"57e359fd421aa95bc338986e",
     * "createdAt":"2016-09-22T12:11:41.613Z",
     * "desc":"程序员不可不知的版权协议。",
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"web",
     * "type":"瞎推荐",
     * "url":"http://www.gcssloop.com/tips/choose-license",
     * "used":true,
     * "who":"sloop"}
     * ],
     * "福利":[
     * {"_id":"57e477fa421aa95bc338987d",
     * "createdAt":"2016-09-23T08:31:54.365Z",
     * "desc":"9-23",
     * "publishedAt":"2016-09-23T11:38:57.170Z",
     * "source":"chrome",
     * "type":"福利",
     * "url":"http://ww3.sinaimg.cn/large/610dc034jw1f837uocox8j20f00mggoo.jpg",
     * "used":true,
     * "who":"daimajia"}
     * ]
     * }
     */

    private List<String> category;
    private boolean error;
    public GankDayResults results;

    public class GankDayResults {

        public List<Gank> Android;
        public List<Gank> iOS;
        public List<Gank> 休息视频;
        public List<Gank> 前端;
        public List<Gank> 拓展资源;
        public List<Gank> 瞎推荐;
        public List<Gank> 福利;
        public List<Gank> App;
    }
}
