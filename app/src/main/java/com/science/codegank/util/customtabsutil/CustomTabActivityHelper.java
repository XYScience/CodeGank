package com.science.codegank.util.customtabsutil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.util.CommonDefine;
import com.science.codegank.util.SharedPreferenceUtil;

import java.util.List;

/**
 * @author SScience
 * @description This is a helper class to manage the connection to the Custom Tabs Service.
 * @email chentushen.science@gmail.com
 * @data 2016/11/26
 */

public class CustomTabActivityHelper implements ServiceConnectionCallback {
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private ConnectionCallback mConnectionCallback;

    /**
     * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView.
     *
     * @param activity The host activity.
     * @param session
     * @param fallback a CustomTabFallback to be used if Custom Tabs is not available.
     */
    public static void openCustomTab(Activity activity,
                                     Gank gank, CustomTabsSession session, CustomTabFallback fallback) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session);
        builder.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
        builder.setSecondaryToolbarColor(activity.getResources().getColor(R.color.colorAccent));
        builder.setShowTitle(true);
        // start 添加一个分享按钮
        String shareLabel = activity.getString(R.string.share_to);
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_share_white);
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.putExtra(Intent.EXTRA_TEXT, gank.getUrl());
        actionIntent.setType("text/plain");
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, actionIntent, 0);
        builder.setActionButton(icon, shareLabel, pendingIntent, true);
        // end
        CustomTabsIntent customTabsIntent = builder.build();

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);

        //If we cant find a package name, it means theres no browser that supports
        //Chrome Custom Tabs installed. So, we fallback to the webview
        if (packageName == null) {
            if (fallback != null) {
                fallback.openUri(activity, Uri.parse(gank.getUrl()), gank.getDesc());
            }
        } else {
            if ((Boolean) SharedPreferenceUtil.get(activity, CommonDefine.SP_KEY_CHROME_CUSTOM_TAB, false)) {
                try {
                    customTabsIntent.intent.setPackage(packageName);
                    customTabsIntent.launchUrl(activity, Uri.parse(gank.getUrl()));
                } catch (ActivityNotFoundException e) {
                    if (fallback != null) {
                        fallback.openUri(activity, Uri.parse(gank.getUrl()), gank.getDesc());
                    }
                }
            } else {
                if (fallback != null) {
                    fallback.openUri(activity, Uri.parse(gank.getUrl()), gank.getDesc());
                }
            }
        }
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service.
     *
     * @param activity the activity that is connected to the service.
     */
    public void unbindCustomTabsService(Activity activity) {
        if (mConnection == null) return;
        activity.unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    public CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(null);
        }
        return mCustomTabsSession;
    }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service.
     *
     * @param connectionCallback
     */
    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.mConnectionCallback = connectionCallback;
    }

    /**
     * Binds the Activity to the Custom Tabs Service.
     *
     * @param activity the activity to be binded to the service.
     */
    public void bindCustomTabsService(Activity activity) {
        if (mClient != null) return;

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName == null) return;

        mConnection = new ServiceConnection(this);
        CustomTabsClient.bindCustomTabsService(activity, packageName, mConnection);
    }

    /**
     * @return true if call to mayLaunchUrl was accepted.
     * @see {@link CustomTabsSession#mayLaunchUrl(Uri, Bundle, List)}.
     */
    public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
        if (mClient == null) return false;

        CustomTabsSession session = getSession();
        if (session == null) return false;

        return session.mayLaunchUrl(uri, extras, otherLikelyBundles);
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mClient = client;
        mClient.warmup(0L);
        if (mConnectionCallback != null) mConnectionCallback.onCustomTabsConnected();
    }

    @Override
    public void onServiceDisconnected() {
        mClient = null;
        mCustomTabsSession = null;
        if (mConnectionCallback != null) mConnectionCallback.onCustomTabsDisconnected();
    }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected.
     */
    public interface ConnectionCallback {
        /**
         * Called when the service is connected.
         */
        void onCustomTabsConnected();

        /**
         * Called when the service is disconnected.
         */
        void onCustomTabsDisconnected();
    }

    /**
     * To be used as a fallback to open the Uri when Custom Tabs is not available.
     */
    public interface CustomTabFallback {
        /**
         * @param activity The Activity that wants to open the Uri.
         * @param uri      The uri to be opened by the fallback.
         */
        void openUri(Activity activity, Uri uri, String title);
    }

}
