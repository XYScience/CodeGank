package com.science.codegank.welfaredetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.science.codegank.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/22
 */

public class WelfareDetailPresenter implements WelfareDetailContract.Presenter {

    private WelfareDetailContract.View mWelfareDetailView;
    private Context mContext;

    public WelfareDetailPresenter(Context context, WelfareDetailContract.View welfareDetailView) {
        mContext = context;
        mWelfareDetailView = welfareDetailView;
    }

    @Override
    public void start() {

    }

    @Override
    public void shareWelfare(String url) {
        if (!TextUtils.isEmpty(url)) {
            new DownLoadWelfareTask().execute(url, "shareWelfare");
        }
    }

    @Override
    public void saveWelfare(String url) {
        if (!TextUtils.isEmpty(url)) {
            new DownLoadWelfareTask().execute(url, "saveWelfare");
        }
    }

    @Override
    public void setWelfareToWallpaper(String url) {
        if (!TextUtils.isEmpty(url)) {
            new DownLoadWelfareTask().execute(url, "setWelfareToWallpaper");
        }
    }

    class DownLoadWelfareTask extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String... strings) {
            String imgName = strings[0].substring(strings[0].lastIndexOf("/") + 1);
            Bitmap bitmap = null;
            try {
                bitmap = Picasso.with(mContext).load(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) {

            }
            // 保存图片
            File imgDir = new File(Environment.getExternalStorageDirectory().toString(), mContext.getString(R.string.app_name));
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
            // 把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), imgName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath()));
            mContext.sendBroadcast(scannerIntent);

            if ("saveWelfare".equals(strings[1])) {
                return strings[1] + "*" + imgDir.getAbsolutePath();
            }
            return strings[1] + "*" + file.getAbsolutePath();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] str = s.split("\\*");
            if (s.contains("saveWelfare")) {
                mWelfareDetailView.saveWelfareSuccess(str[1]);
            } else if (s.contains("setWelfareToWallpaper")) {
                mWelfareDetailView.setWelfareToWallpaperSuccess(str[1]);
            } else if (s.contains("shareWelfare")) {
                Uri imgUri = Uri.fromFile(new File(s));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, imgUri);
                intent.setType("image/*");
                mContext.startActivity(Intent.createChooser(intent, "分享到"));
            }
        }
    }
}
