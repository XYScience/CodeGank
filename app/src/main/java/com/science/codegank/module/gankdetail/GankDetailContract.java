package com.science.codegank.module.gankdetail;

import android.webkit.WebView;
import android.widget.FrameLayout;

import com.science.codegank.base.BasePresenter;
import com.science.codegank.base.BaseView;
import com.science.codegank.data.bean.Gank;

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
        void setUpWebView(WebView webView, FrameLayout mVideoWebView);

        void loadUrl(WebView webView, String url);

        void shareGank(String url);

        void openInBrowser(String url);

        boolean inCustomView();

        void hideCustomView();

        void collectArticle(Gank gank);
    }
}
