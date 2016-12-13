package com.science.codegank.module.welfaredetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.science.codegank.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/22
 */

public class WelfareDetailModel {

    interface WelfareDownLoadCallback {
        void onWelfareDownLoadSuccess(String path);
    }

    public void welfareDetailDownLoad(final Context context, String url, String path, final WelfareDownLoadCallback callback) {
        new AsyncTask<String, Object, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String imgName = strings[0].substring(strings[0].lastIndexOf("/") + 1);
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(strings[0]).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    // 保存图片
                    File imgDir = new File(Environment.getExternalStorageDirectory().toString(), context.getString(R.string.app_name));
                    if (!imgDir.exists()) {
                        imgDir.mkdir();
                    }
                    File file = new File(imgDir, imgName);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                // 把文件插入到系统图库
//                try {
//                    MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), imgName, null);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                // 通知图库更新
//                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath()));
//                context.sendBroadcast(scannerIntent);

                    if ("saveWelfare".equals(strings[1])) {
                        return strings[1] + "*" + imgDir.getAbsolutePath();
                    }
                    return strings[1] + "*" + file.getAbsolutePath();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] str = s.split("\\*");
                if (str[0].equals("shareWelfare")) {
                    // 分享
                    callback.onWelfareDownLoadSuccess(str[1]);
                } else if (str[0].equals("setWelfareToWallpaper")) {
                    // 设置壁纸
                    callback.onWelfareDownLoadSuccess(str[1]);
                } else if (str[0].equals("saveWelfare")) {
                    // 保存
                    callback.onWelfareDownLoadSuccess(str[1]);
                }
            }
        }.execute(url, path);
    }
}
