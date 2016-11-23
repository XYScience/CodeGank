package com.science.codegank.welfaredetail;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/22
 */

public class WelfareDetailPresenter implements WelfareDetailContract.Presenter {

    private WelfareDetailContract.View mWelfareDetailView;
    private Context mContext;
    private WelfareDetailModel mWelfareDetailModel;

    public WelfareDetailPresenter(Context context, WelfareDetailContract.View welfareDetailView) {
        mContext = context;
        mWelfareDetailView = welfareDetailView;
        mWelfareDetailModel = new WelfareDetailModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void shareWelfare(String url) {
        if (!TextUtils.isEmpty(url)) {
            mWelfareDetailModel.welfareDetailDownLoad(mContext, url, "shareWelfare", new WelfareDetailModel.WelfareDownLoadCallback() {
                @Override
                public void onWelfareDownLoadSuccess(String path) {
                    Uri imgUri = Uri.fromFile(new File(path));
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, imgUri);
                    intent.setType("image/*");
                    mContext.startActivity(Intent.createChooser(intent, "分享到"));
                }
            });
        }
    }

    @Override
    public void saveWelfare(String url) {
        if (!TextUtils.isEmpty(url)) {
            mWelfareDetailModel.welfareDetailDownLoad(mContext, url, "saveWelfare", new WelfareDetailModel.WelfareDownLoadCallback() {
                @Override
                public void onWelfareDownLoadSuccess(String path) {
                    mWelfareDetailView.saveWelfareSuccess(path);
                }
            });
        }
    }

    @Override
    public void setWelfareToWallpaper(String url) {
        if (!TextUtils.isEmpty(url)) {
            mWelfareDetailModel.welfareDetailDownLoad(mContext, url, "setWelfareToWallpaper", new WelfareDetailModel.WelfareDownLoadCallback() {
                @Override
                public void onWelfareDownLoadSuccess(String path) {
                    /**
                     * 对于面向 Android N 的应用，Android 框架执行的 StrictMode，API 禁止向您的应用外公开 file://URI。
                     * 如果一项包含文件 URI 的 Intent 离开您的应用，应用失败，并出现 FileUriExposedException异常。
                     * 若要在应用间共享文件，您应发送一项 content://URI，并授予 URI 临时访问权限。
                     * 进行此授权的最简单方式是使用 FileProvider类。
                     */
                    WallpaperManager wm = WallpaperManager.getInstance(mContext);
                    Uri uri = FileProvider.getUriForFile(mContext, "com.science.codegank.fileprovider", new File(path));
                    mContext.startActivity(wm.getCropAndSetWallpaperIntent(uri));
                    //后台设置壁纸:
                    //Bitmap bitmap = BitmapFactory.decodeFile(path);
                    //wm.setBitmap(bitmap);
                }
            });
        }
    }
}
