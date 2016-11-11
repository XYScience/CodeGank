package com.science.codegank.category;

import com.science.codegank.base.BasePresenter;
import com.science.codegank.base.BaseView;
import com.science.codegank.data.bean.BaseData;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/10
 */

public interface CategoryContract {

    interface View<T extends BaseData> extends BaseView<Presenter> {
        void getCategoryData(boolean isFirst, List<T> data);
        void refreshFinish();
    }

    interface Presenter extends BasePresenter {
        void getCategoryData(String category, int page);
    }
}
