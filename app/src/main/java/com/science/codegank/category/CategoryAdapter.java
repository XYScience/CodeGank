package com.science.codegank.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.science.baserecyclerviewadapter.base.BaseCommonAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.util.CommonUtil;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/11
 */

public class CategoryAdapter extends BaseCommonAdapter<List<Gank>> {

    private Context mContext;

    public CategoryAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        mContext = context;
    }

    @Override
    public void convertCommon(ViewHolder viewHolder, List<Gank> ganks, int i) {
        Gank gank = ganks.get(i);
        viewHolder.setText(R.id.tv_desc, gank.getDesc());
        viewHolder.setText(R.id.tv_who, gank.getWho());
        viewHolder.setText(R.id.tv_date, CommonUtil.toDate(gank.getPublishedAt()));
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_gank_category;
    }
}
