package com.science.codegank.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/9
 */

public class ImageLoadUtil {

    public static void loadImage(Context context, String url, int placeholder, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(new GlideDrawableImageViewTarget(imageView)); // 解决第一次加载图片拉伸问题
//                .into(new SimpleTarget<GlideDrawable>() { // 解决CircleImageView（第三方自定义的圆形ImageView）在使用Glide的占位图是不显示问题
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        imageView.setImageDrawable(resource);
//                    }
//                });
    }

    public static void loadCircleImage(Context context, int img, ImageView imageView) {
        Glide.with(context).load(img).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }

}
