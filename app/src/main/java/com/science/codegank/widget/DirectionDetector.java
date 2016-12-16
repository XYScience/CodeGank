package com.science.codegank.widget;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public class DirectionDetector {
    public static int getDirection(int i, boolean z) {
        int i2 = 0;
        if (i > 0) {
            i2 = 1;
        }
        if (!z || i >= 0) {
            return i2;
        }
        return 2;
    }

    public int getDirection(int i, boolean z, ScrollStateChangedListener scrollStateChangedListener) {
        int direction = getDirection(i, z);
        if (scrollStateChangedListener != null) {
            scrollStateChangedListener.onChildDirectionChange(direction);
        }
        return direction;
    }
}
