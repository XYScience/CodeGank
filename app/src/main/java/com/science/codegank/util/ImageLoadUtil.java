package com.science.codegank.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/9
 */

public class ImageLoadUtil {

    public static void loadImage(Context context, String url, int placeholder, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(new GlideDrawableImageViewTarget(imageView));
    }

}
