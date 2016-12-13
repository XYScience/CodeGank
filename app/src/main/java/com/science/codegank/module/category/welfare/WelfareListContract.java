package com.science.codegank.module.category.welfare;

import com.science.codegank.base.BasePresenter;
import com.science.codegank.base.BaseView;
import com.science.codegank.data.bean.BaseData;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public interface WelfareListContract {

    interface View<T extends BaseData> extends BaseView<Presenter> {
        void getWelfareData(boolean isFirst, List<T> data);

        void hasNoMoreData();

        void refreshFinish();
    }

    interface Presenter extends BasePresenter {
        void getWelfareData(String category, int page);
    }
}
