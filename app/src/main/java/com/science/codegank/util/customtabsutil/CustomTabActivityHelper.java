package com.science.codegank.util.customtabsutil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import com.science.codegank.R;
import com.science.codegank.data.bean.BaseData;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.SearchResult;
import com.science.codegank.receiver.CustomTabsBroadcastReceiver;
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
    public static void openCustomTab(Activity activity, BaseData data, CustomTabsSession session,
                                     CustomTabFallback fallback) {
        String url = null, title = null;
        if (data instanceof Gank) {
            Gank gank = (Gank) data;
            url = gank.getUrl();
            title = gank.getDesc();
        } else if (data instanceof SearchResult) {
            SearchResult searchResult = (SearchResult) data;
            url = searchResult.getUrl();
            title = searchResult.getDesc();
        }
        if ((Boolean) SharedPreferenceUtil.get(activity, CommonDefine.SP_KEY_CHROME_CUSTOM_TAB, false)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session);
            builder.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
            builder.setSecondaryToolbarColor(activity.getResources().getColor(R.color.colorAccent));
            builder.setShowTitle(true);

            // start 添加一个收藏到options menu
            String collectLabel = activity.getString(R.string.collections);
            Intent collectIntent = new Intent(activity, CustomTabsBroadcastReceiver.class);
            collectIntent.putExtra(CustomTabsBroadcastReceiver.EXTRA_RECEIVER, CustomTabsBroadcastReceiver.COLLECT);
            // 2nd Parameter is the requestCode which should be different for different broadcast
            PendingIntent collectPendingIntent = PendingIntent.getBroadcast(activity, CustomTabsBroadcastReceiver.COLLECT, collectIntent, 0);
            builder.addMenuItem(collectLabel, collectPendingIntent);
            // end
            // start 添加一个分享到options menu
            String shareLabel = activity.getString(R.string.share_to);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            shareIntent.setType("text/plain");
            PendingIntent sharePendingIntent = PendingIntent.getActivity(activity, 0, shareIntent, 0);
            builder.addMenuItem(shareLabel, sharePendingIntent);
            // end
            // start 添加一个复制链接到options menu
            String copyLabel = activity.getString(R.string.copy_link);
            Intent copyIntent = new Intent(activity, CustomTabsBroadcastReceiver.class);
            copyIntent.putExtra(CustomTabsBroadcastReceiver.EXTRA_RECEIVER, CustomTabsBroadcastReceiver.COPY);
            // 2nd Parameter is the requestCode which should be different for different broadcast
            PendingIntent copyPendingIntent = PendingIntent.getBroadcast(activity, CustomTabsBroadcastReceiver.COPY, copyIntent, 0);
            builder.addMenuItem(copyLabel, copyPendingIntent);
            // end
            CustomTabsIntent customTabsIntent = builder.build();

            String packageName = CustomTabsHelper.getPackageNameToUse(activity);
            try {
                customTabsIntent.intent.setPackage(packageName);
                customTabsIntent.launchUrl(activity, Uri.parse(url));
            } catch (ActivityNotFoundException e) {
                if (fallback != null) {
                    fallback.openUri(activity, Uri.parse(url), title);
                }
            }
        } else {
            if (fallback != null) {
                fallback.openUri(activity, Uri.parse(url), title);
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
