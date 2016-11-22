package com.science.codegank.welfaredetail;

import com.science.codegank.base.BasePresenter;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/22
 */

public interface WelfareDetailContract {

    interface View {
        void saveWelfareSuccess();

        void setWelfareToWallpaperSuccess();
    }

    interface Presenter extends BasePresenter {
        void shareWelfare();

        void saveWelfare(String url);

        void setWelfareToWallpaper();
    }
}
