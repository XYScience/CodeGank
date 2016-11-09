package com.science.codegank.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.science.codegank.R;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/9
 */

public class ImageLoadUtil {

    public static void loadImage(Context context, String url, ImageView imageView ) {
        Glide.with(context)
                .load(url)
//                .centerCrop()
                .placeholder(R.drawable.welfare)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

}
