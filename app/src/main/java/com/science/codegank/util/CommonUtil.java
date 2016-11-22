package com.science.codegank.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class CommonUtil {

    public static int dipToPx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static String toDate(String date) {
        String[] s = date.split("T");
        return s[0];
//        DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
//        return dateFormat.format(date);
    }

    private static View mStatusBarView;

    public static View setStatusBarColor(Activity activity, int statusBarColor) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        mStatusBarView = contentView.getChildAt(0);
        if (mStatusBarView != null && mStatusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
            mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
            return mStatusBarView;
        }
        mStatusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
        contentView.addView(mStatusBarView, lp);
        return mStatusBarView;
    }

    public static View setTranslucentStatusBar(Activity activity, int statusBarColor) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
        contentView.addView(statusBarView, lp);
        return statusBarView;
    }

    public static int getWindowScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static void animateIn(final View v, @AnimRes int anim) {
        if (v.getVisibility() == View.VISIBLE) {
            return;
        }

        v.clearAnimation();

        Animation animation = AnimationUtils.loadAnimation(v.getContext(), anim);

        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }
        });

        v.startAnimation(animation);
    }

    public static void animateOut(final View v, @AnimRes int anim) {
        if (v.getVisibility() != View.VISIBLE) {
            return;
        }

        v.clearAnimation();

        Animation animation = AnimationUtils.loadAnimation(v.getContext(), anim);

        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.INVISIBLE);
            }
        });

        v.startAnimation(animation);
    }

    public static class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

    private static final int FLAG_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    public static void setSystemUiVisible(Activity activity) {
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(decor.getSystemUiVisibility() | FLAG_IMMERSIVE);
    }

    public static void setSystemUiInVisible(Activity activity) {
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(decor.getSystemUiVisibility() & ~FLAG_IMMERSIVE);
    }
}
