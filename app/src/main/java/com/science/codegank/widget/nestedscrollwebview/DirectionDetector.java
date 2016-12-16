package com.science.codegank.widget.nestedscrollwebview;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/12/16
 */

public class DirectionDetector {
    public static int getDirection(int paramInt, boolean paramBoolean) {
        int i = 0;
        if (paramInt > 0) {
            i = 1;
        }
        if (paramBoolean && paramInt < 0) {
            i = 2;
        }
        return i;
    }
    public int getDirection(int paramInt, boolean paramBoolean, ScrollStateChangedListener paramc) {
        int direction = getDirection(paramInt, paramBoolean);
        if (paramc != null)
            paramc.onChildDirectionChange(direction);
        return direction;
    }
}
