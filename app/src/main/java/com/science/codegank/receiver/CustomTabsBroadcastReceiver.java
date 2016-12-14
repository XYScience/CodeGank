package com.science.codegank.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.science.codegank.R;
import com.science.codegank.util.CommonUtil;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/14
 */

public class CustomTabsBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_RECEIVER = "receiver";
    public static final int COLLECT = 0;
    public static final int COPY = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getIntExtra(EXTRA_RECEIVER, -1) == COLLECT) {
            Toast.makeText(context, context.getString(R.string.collect_success), Toast.LENGTH_SHORT).show();
        } else if (intent.getIntExtra(EXTRA_RECEIVER, -1) == COPY) {
            String copyLink = intent.getDataString();
            CommonUtil.copyToClipBoard(context, copyLink);
            Toast.makeText(context, context.getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
        }
    }
}
