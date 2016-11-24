package com.science.codegank.welfaredetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.science.codegank.R;

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
                    mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_to)));
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
                    mWelfareDetailView.setWelfareToWallpaper(path);
                }
            });
        }
    }
}
