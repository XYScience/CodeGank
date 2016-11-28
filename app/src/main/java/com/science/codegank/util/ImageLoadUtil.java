package com.science.codegank.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/9
 */

public class ImageLoadUtil {

    public static void loadImage(Context context, String url, int placeholder, final ImageView imageView) {
        DrawableRequestBuilder builder = Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade();
        if (placeholder == 0) {
            builder.into(imageView);
        } else {
            // 解决设置占位图placeholder时首次加载图片拉伸问题
            // 解决CircleImageView（第三方自定义的圆形ImageView）在使用Glide的占位图时不显示问题
            builder.into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }

    public static void loadCircleImage(Context context, int img, ImageView imageView) {
        Glide.with(context).load(img).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }

    /**
     * 清除图片所有缓存
     */
    public static void clearImageAllCache(Context context, TextView textView) {
        clearImageDiskCache(context, textView);
        clearImageMemoryCache(context);
    }

    /**
     * 清除图片磁盘缓存
     * 只能在子线程执行
     */
    public static void clearImageDiskCache(final Context context, final TextView textView) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new AsyncTask<Boolean, Boolean, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Boolean... booleen) {
                        Glide.get(context).clearDiskCache();
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean && textView != null) {
                            textView.setText(ImageLoadUtil.getCacheSize(context));
                        }
                    }
                }.execute();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     * 只能在主线程执行
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getExternalCacheDir() + "/glideCache")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
