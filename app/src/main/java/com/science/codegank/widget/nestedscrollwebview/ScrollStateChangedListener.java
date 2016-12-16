package com.science.codegank.widget.nestedscrollwebview;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public interface ScrollStateChangedListener {
    void onChildDirectionChange(int position);

    void onChildPositionChange(ScrollState parama);

    enum ScrollState {TOP, BOTTOM, MIDDLE, NO_SCROLL}
}
