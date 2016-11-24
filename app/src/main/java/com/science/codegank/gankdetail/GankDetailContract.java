package com.science.codegank.gankdetail;

import android.webkit.WebView;

import com.science.codegank.base.BasePresenter;
import com.science.codegank.base.BaseView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/24
 */

public interface GankDetailContract {

    interface View extends BaseView<Presenter> {

        void loadStart();

        void loadWebViewFinished();
    }

    interface Presenter extends BasePresenter {
        void setUpWebView(WebView webView);

        void loadUrl(WebView webView, String url);
    }
}
