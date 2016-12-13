package com.science.codegank.module.gankdetail;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.science.codegank.R;
import com.science.codegank.util.CommonDefine;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.SharedPreferenceUtil;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/24
 */

public class GankDetailPresenter implements GankDetailContract.Presenter {

    private GankDetailContract.View mGankDetailView;
    private Context mContext;
    private MyWebChromeClient mMyWebChromeClient;
    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    public GankDetailPresenter(Context context, GankDetailContract.View gankDetailView) {
        mGankDetailView = gankDetailView;
        mGankDetailView.setPresenter(this);
        mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void setUpWebView(WebView webView, FrameLayout videoWebView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        if ((Boolean) SharedPreferenceUtil.get(mContext, CommonDefine.SP_KEY_SMART_NO_PIC, false)) {
            settings.setBlockNetworkImage(true);
        } else {
            settings.setBlockNetworkImage(false);
        }
        webView.setWebViewClient(new MyWebClient());

        mMyWebChromeClient = new MyWebChromeClient(webView, videoWebView);
        webView.setWebChromeClient(mMyWebChromeClient);
    }

    @Override
    public void loadUrl(WebView webView, String url) {
        webView.loadUrl(url);
    }

    @Override
    public void shareGank(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_to)));
    }

    @Override
    public void openInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(intent);
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request.getUrl() == null) {
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mGankDetailView.loadStart();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mGankDetailView.loadWebViewFinished();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mGankDetailView.getDataError(description);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        FrameLayout mVideoWebView;
        WebView mWebView;

        public MyWebChromeClient(WebView webView, FrameLayout videoWebView) {
            mVideoWebView = videoWebView;
            mWebView = webView;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            ((GankDetailActivity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mWebView.setVisibility(View.GONE);
            //如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mVideoWebView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            mVideoWebView.setVisibility(View.VISIBLE);

            ((GankDetailActivity) mContext).mToolbar.setVisibility(View.GONE);
            CommonUtil.setSystemUiVisible(((GankDetailActivity) mContext));
        }

        @Override
        public void onHideCustomView() {
            if (xCustomView == null)//不是全屏播放状态
                return;
            // Hide the custom view.
            ((GankDetailActivity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            mVideoWebView.removeView(xCustomView);
            xCustomView = null;
            mVideoWebView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            mWebView.setVisibility(View.VISIBLE);

            ((GankDetailActivity) mContext).mToolbar.setVisibility(View.VISIBLE);
            CommonUtil.setSystemUiInVisible(((GankDetailActivity) mContext));
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    @Override
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    @Override
    public void hideCustomView() {
        mMyWebChromeClient.onHideCustomView();
    }
}
