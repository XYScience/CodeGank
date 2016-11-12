package com.science.codegank.searchresult;

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

public interface SearchResultContract {

    interface View<T extends BaseData> extends BaseView<Presenter> {
        void getSearchResultData(boolean isFirst, List<T> data);
        void refreshFinish();
    }

    interface Presenter extends BasePresenter {
        void getSearchResultData(String query, String category, int page);
    }

}
