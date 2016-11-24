package com.science.codegank.view;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.science.codegank.gankdetail.GankDetailActivity;
import com.science.codegank.homeday.HomeFragment;

/**
 * @author SScience
 * @description A Fallback that opens a Webview when Custom Tabs is not available
 * @email chentushen.science@gmail.com
 * @data 2016/11/24
 */

public class WebViewFallback implements HomeFragment.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri, String title) {
        Intent intent = new Intent(activity, GankDetailActivity.class);
        intent.putExtra(HomeFragment.EXTRA_BUNDLE_URL, uri.toString());
        intent.putExtra(HomeFragment.EXTRA_BUNDLE_DESC, title);
        activity.startActivity(intent);
    }
}
