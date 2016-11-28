package com.science.codegank.gankdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.science.codegank.R;
import com.science.codegank.util.CommonDefine;
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

    public GankDetailPresenter(Context context, GankDetailContract.View gankDetailView) {
        mGankDetailView = gankDetailView;
        mGankDetailView.setPresenter(this);
        mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void setUpWebView(WebView webView) {
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
}
