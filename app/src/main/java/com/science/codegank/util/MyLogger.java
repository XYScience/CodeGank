package com.science.codegank.util;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * @author SScience
 * @description 显示超链接、养眼log
 * @email chentushen.science@gmail.com
 * @data 2016/9/25
 */

public class MyLogger {

    private static boolean IS_DEBUG = true;

    /**
     * 最终的打印方法
     * 打印内容：tag:>>>>>>>>>>;
     * msg:方法名(类名.java：行数): 要打印的信息
     *
     * @param msg 信息
     */
    private static void takeLogE(String methodLine, String msg) {
        Log.e(">>>>>>>>>>", methodLine + msg);
    }

    public static void e(Object msg) {
        if (IS_DEBUG) {
            String methodLine = getMethodAndLine();
            takeLogE(methodLine, String.valueOf(msg));
        }
    }

    /**
     * 打印方法的类名
     *
     * @return 类名前缀
     */
    private static String getClassName() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String className = traceElement.getFileName();
        //去除文件名中的后缀
        if (className.contains(".java")) {
            className = className.substring(0, className.length() - 5);
        }
        return className;
    }

    /**
     * 打印的方法+类名+所在行数(显示超链)
     */
    private static String getMethodAndLine() {
        String result = "";
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        result += traceElement.getMethodName();
        result += "(" + traceElement.getFileName();
        result += ":" + traceElement.getLineNumber() + "): ";
        return result;
    }

    /**
     * 打印分割线,主要用于程序启动或销毁时在log中做标记
     *
     * @param msg 信息
     */
    public static void line(String msg) {
        if (IS_DEBUG) {
            takeLogE("---====================================---\n" +
                    "\t我是" + msg + "的分割线-" + getCurrentTime() +
                    "\n---====================================---", null);
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(System.currentTimeMillis());
    }
}
