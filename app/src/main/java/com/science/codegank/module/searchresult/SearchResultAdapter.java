package com.science.codegank.module.searchresult;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.science.baserecyclerviewadapter.base.BaseCommonAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.SearchResult;
import com.science.codegank.util.CommonUtil;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/11
 */

public class SearchResultAdapter extends BaseCommonAdapter<List<SearchResult>> {

    private Context mContext;

    public SearchResultAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        mContext = context;
    }

    @Override
    public void convertCommon(ViewHolder viewHolder, List<SearchResult> list, int i) {
        SearchResult searchResult = list.get(i);
        viewHolder.setText(R.id.tv_desc, searchResult.getDesc());
        viewHolder.setText(R.id.tv_who, searchResult.getWho());
        viewHolder.setText(R.id.tv_date, CommonUtil.toDate(searchResult.getPublishedAt()));
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_gank_category;
    }
}
