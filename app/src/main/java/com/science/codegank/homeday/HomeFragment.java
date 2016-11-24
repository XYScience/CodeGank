package com.science.codegank.homeday;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.baserecyclerviewadapter.interfaces.OnLoadMoreListener;
import com.science.baserecyclerviewadapter.widget.StickyHeaderItemDecoration;
import com.science.codegank.MainActivity;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;
import com.science.codegank.view.OnDoubleClickListener;
import com.science.codegank.view.RatioImageView;
import com.science.codegank.view.WebViewFallback;
import com.science.codegank.welfaredetail.WelfareDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;

import static android.support.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.science.codegank.R.id.recyclerView;
import static com.science.codegank.R.id.toolbar;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class HomeFragment extends BaseFragment implements HomeContract.View<GankDayResults> {

    public static final String EXTRA_BUNDLE_DESC = "bundle_desc";
    public static final String EXTRA_BUNDLE_URL = "bundle_url";
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private View mRootView;
    private HomeContract.Presenter mHomePresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void doCreateView(final View view) {
        mRootView = view;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new StickyHeaderItemDecoration());
        mHomeAdapter = new HomeAdapter(getActivity());
        mHomeAdapter.setOnItemClickListener(new OnItemClickListener<GankDayResults>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, GankDayResults ganks, int i) {
                if (getResources().getString(R.string.welfare).equals(ganks.getHeader())) {
                    WelfareDetailActivity.intentWelfareDetail(getActivity(),
                            ganks.getGankList().get(i).getUrl(),
                            CommonUtil.toDate(ganks.getGankList().get(i).getPublishedAt()),
                            viewHolder.getView(R.id.iv_day_welfare));
                } else {
                    intentToGankDetail(ganks.getGankList().get(i));
                }
            }

            @Override
            public void onItemEmptyClick() {
                mHomePresenter.start();
            }
        });
        mHomeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i) {
                mHomePresenter.getGankDayDataMore();
            }
        });
        mRecyclerView.setAdapter(mHomeAdapter);

        if (mHomePresenter != null) {
            mHomePresenter.start();
        }

        getActivity().findViewById(toolbar).setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onClicks(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        initRefreshLayout(view);
    }

    private void intentToGankDetail(Gank gank) {
        //Intent intent = new Intent(getActivity(), GankDetailActivity.class);
        //intent.putExtra(EXTRA_BUNDLE_URL, gank.getUrl());
        //intent.putExtra(EXTRA_BUNDLE_DESC, gank.getDesc());
        //startActivity(intent);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setSecondaryToolbarColor(getResources().getColor(R.color.colorAccent));
        builder.setShowTitle(true);
        // 添加一个分享按钮
        String shareLabel = getString(R.string.share_to);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white);
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.putExtra(Intent.EXTRA_TEXT, gank.getUrl());
        actionIntent.setType("text/plain");
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, actionIntent, 0);
        builder.setActionButton(icon, shareLabel, pendingIntent, true);
        // end
        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.launchUrl(getActivity(), Uri.parse(gank.getUrl()));

        openCustomTab(getActivity(), customTabsIntent, Uri.parse(gank.getUrl()), gank.getDesc(), new WebViewFallback());
    }

    /**
     * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView.
     *
     * @param activity         The host activity.
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available.
     * @param uri              the Uri to be opened.
     * @param fallback         a CustomTabFallback to be used if Custom Tabs is not available.
     */
    public static void openCustomTab(Activity activity,
                                     CustomTabsIntent customTabsIntent,
                                     Uri uri,
                                     String title,
                                     CustomTabFallback fallback) {
        String packageName = getPackageNameToUse(activity);

        //If we cant find a package name, it means theres no browser that supports
        //Chrome Custom Tabs installed. So, we fallback to the webview
        if (packageName == null) {
            if (fallback != null) {
                fallback.openUri(activity, uri, title);
            }
        } else {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        }
    }

    private static String sPackageNameToUse;
    static final String STABLE_PACKAGE = "com.android.chrome";
    static final String BETA_PACKAGE = "com.chrome.beta";
    static final String DEV_PACKAGE = "com.chrome.dev";
    static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     * <p>
     * This is <strong>not</strong> threadsafe.
     *
     * @param context {@link Context} to use for accessing {@link PackageManager}.
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    public static String getPackageNameToUse(Context context) {
        if (sPackageNameToUse != null) return sPackageNameToUse;

        PackageManager pm = context.getPackageManager();
        // Get default VIEW intent handler.
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
        String defaultViewHandlerPackageName = null;
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        // Get all apps that can handle VIEW intents.
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        if (packagesSupportingCustomTabs.isEmpty()) {
            sPackageNameToUse = null;
        } else if (packagesSupportingCustomTabs.size() == 1) {
            sPackageNameToUse = packagesSupportingCustomTabs.get(0);
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            sPackageNameToUse = defaultViewHandlerPackageName;
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            sPackageNameToUse = STABLE_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            sPackageNameToUse = BETA_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            sPackageNameToUse = DEV_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            sPackageNameToUse = LOCAL_PACKAGE;
        }
        return sPackageNameToUse;
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     *
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private static boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
        try {
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> handlers = pm.queryIntentActivities(
                    intent,
                    PackageManager.GET_RESOLVED_FILTER);
            if (handlers == null || handlers.size() == 0) {
                return false;
            }
            for (ResolveInfo resolveInfo : handlers) {
                IntentFilter filter = resolveInfo.filter;
                if (filter == null) continue;
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue;
                if (resolveInfo.activityInfo == null) continue;
                return true;
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "Runtime exception while getting specialized handlers");
        }
        return false;
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

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        if (presenter != null) {
            mHomePresenter = presenter;
        }
    }

    @Override
    public void getGankDayData(boolean isFirst, List<GankDayResults> data) {
        if (isFirst) {
            final Gank gank = data.get(0).getGankList().get(0);
            String todayWelfareUrl = gank.getUrl();
            final RatioImageView ivWelfareToday = ((MainActivity) getActivity()).mIvWelfareToday;
            ImageLoadUtil.loadImage(getActivity(), todayWelfareUrl, R.drawable.welfare, ivWelfareToday);
            final TextView tvTimeToday = (TextView) getActivity().findViewById(R.id.tv_time_today);
            tvTimeToday.setText(CommonUtil.toDate(gank.getPublishedAt()));
            CommonUtil.animateIn(tvTimeToday, R.anim.viewer_toolbar_fade_in);
            data.remove(0);
            mHomeAdapter.setData(false, data);
            ivWelfareToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WelfareDetailActivity.intentWelfareDetail(getActivity(),
                            gank.getUrl(),
                            CommonUtil.toDate(gank.getPublishedAt()),
                            ivWelfareToday);
                }
            });
        } else {
            mHomeAdapter.setData(true, data);
        }
    }

    @Override
    public void addSubscription(Subscription subscription) {
        addCompositeSubscription(subscription);
    }

    @Override
    public void hasNoMoreData() {
        mHomeAdapter.showFooterNoMoreData();
    }

    @Override
    public void getDataError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        mHomeAdapter.showLoadFailed();
        setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mHomePresenter.start();
    }

    @Override
    public void refreshFinish() {
        setRefreshing(false);
    }
}
