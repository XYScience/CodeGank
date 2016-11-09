package com.science.codegank.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/9
 */

public class ImageLoadUtil {

    public static void loadImage(Context context, String url, int placeholderImg, ImageView imageView) {
        Glide.with(context)
                .load(url)
                //.asBitmap() //当设置placeholder占位图时，Glide首次加载图片变形/拉伸解决办法
                .centerCrop()
                .placeholder(placeholderImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

}
