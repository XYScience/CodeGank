package com.science.codegank.view;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * @author 幸运Science
 * @description glid缓存
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/9
 */

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        File cacheDir = context.getExternalCacheDir(); //数据的缓存地址
        int diskCacheSize = 1024 * 1024 * 200;
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
