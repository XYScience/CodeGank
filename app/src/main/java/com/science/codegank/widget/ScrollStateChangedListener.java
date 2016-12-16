package com.science.codegank.widget;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public interface ScrollStateChangedListener {
    public enum ScrollState {
        TOP,
        BOTTOM,
        MIDDLE,
        NO_SCROLL
    }

    void onChildDirectionChange(int i);

    void onChildPositionChange(ScrollState scrollState);
}
