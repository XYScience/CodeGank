package com.science.codegank.util;

import android.content.Context;

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
}
